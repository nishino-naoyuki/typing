package jp.ac.asojuku.typing.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.entity.AnsHistoryTblEntity;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventUserEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.form.QuestionForm;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.param.TypingConst;
import jp.ac.asojuku.typing.repository.AnsTblRepository;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.specifications.AnsSpecifications;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.repository.specifications.QuestionSpecifications;
import jp.ac.asojuku.typing.util.Exchange;

@Service
public class QuestionService extends ServiceBase{
	Logger logger = LoggerFactory.getLogger(QuestionService.class);
	
	/**
	 * 問題を1件挿入
	 * @param form
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(QuestionForm form) {
		QestionTblEntity qEntity = questionRepository.save(getFrom(form));
		
		//練習問題としての登録があるか？
		EventQuestionEntity pqEntity = eventQuestionRepository.findByQidAndEidIsNull(qEntity.getQid());;
		if( form.getPracticeFlg() ) {
			//練習問題の場合は、イベントを仮登録する。既にある場合するひつようはない
			if(pqEntity == null) {
				pqEntity = new EventQuestionEntity();
				pqEntity.setQid(qEntity.getQid());
				pqEntity.setEid(null);
				pqEntity.setNo(0);
				eventQuestionRepository.save(pqEntity);
			}
		}else {
			if( pqEntity != null ) {
				//練習問題ではないのにNULLイベントの登録があるということは　練習問題→本番用の問題　に切り替えられたということなので
				//リンクを削除しておく
				eventQuestionRepository.delete(pqEntity);
			}
		}
	}
	
	/**
	 * 問題リストを取得する
	 * 学生は、練習問題のみ
	 * 先生は全部の問題を取得する
	 * @param uid
	 * @param role
	 * @return
	 */
	public List<QuestionOutlineDto> list(Integer uid,RoleId role){
		List<QuestionOutlineDto> list = new ArrayList<>();
		
		List<QestionTblEntity> entityList =  questionRepository.getQList(role.getId());
		for(QestionTblEntity entity : entityList) {
			list.add(getFrom(entity,uid,TypingConst.PRACTICE_EVENTID));
		}
		return list;
	}

	/**
	 * イベント作成時にイベント用の問題リストを取得する
	 * @return
	 */
	public List<QuestionOutlineDto> listForEvent(){
		List<QuestionOutlineDto> list = new ArrayList<>();
		List<QestionTblEntity> entityList =  questionRepository.findByPracticeflgOrderByTitle(0);
		for(QestionTblEntity entity : entityList) {
			list.add(getFrom(entity));
		}
		return list;
	}
	
	/**
	 * 練習問題の詳細を取得する
	 * 
	 * @param qid
	 * @param role
	 * @param uid
	 * @return
	 */
	public QuestionDetailDto getPracticeDetail(Integer qid,RoleId role,Integer uid) {
		QestionTblEntity qEntity = questionRepository.findOne(
				Specification.
				where( QuestionSpecifications.qidEquals(qid)
				.and(QuestionSpecifications.practiceEquals((role == RoleId.STUDENT ?1:null))) )
				).orElse(null);
		
		return getDetailForm(qEntity,uid,TypingConst.PRACTICE_EVENTID);
	}

	/**
	 * @param qid
	 * @param role
	 * @param uid
	 * @return
	 */
	public QuestionDetailDto getDetail(Integer qid) {
		QestionTblEntity qEntity = questionRepository.findOne(
				Specification.
				where( QuestionSpecifications.qidEquals(qid) )
				).orElse(null);
		
		return getDetailForm(qEntity);
	}
	
	/**
	 * @param uid
	 * @param eid
	 */
	public void entryEvent(Integer uid,Integer eid) {
		
		EventUserEntity entity = new EventUserEntity();
		
		entity.setDelFlg(0);
		entity.setEid(eid);
		entity.setUid(uid);
		
		eventUserRepository.save(entity);
	}
	
	/* ------- private method ------------ */
	
	private QuestionOutlineDto getFrom(QestionTblEntity entity,Integer uid,Integer eid) {
		QuestionOutlineDto dto = new QuestionOutlineDto();
		
		dto.setQid(entity.getQid());
		dto.setDifficulty(entity.getDifficalty());
		dto.setPracticeFlg(entity.getPracticeflg());
		dto.setTitle(entity.getTitle());
		dto.setKindName( (entity.getPracticeflg()==1?"練習用":"大会用") );
		AnsHistoryTblEntity ansEntity = ansHistoryTblRepository.getRecentlyOne(eid,entity.getQid(),uid);
		if( ansEntity != null ) {
			dto.setSubmitTime(ansEntity.getAnsTimestamp());
			dto.setSubmitTimeString(Exchange.toFormatString(ansEntity.getAnsTimestamp(), "yyyy/MM/dd HH:mm:ss"));
		}else {
			dto.setSubmitTime(null);
			dto.setSubmitTimeString("未解答");
		}
		
		return dto;
	}
	private QuestionOutlineDto getFrom(QestionTblEntity entity) {
		QuestionOutlineDto dto = new QuestionOutlineDto();
		
		dto.setQid(entity.getQid());
		dto.setDifficulty(entity.getDifficalty());
		dto.setPracticeFlg(entity.getPracticeflg());
		dto.setTitle(entity.getTitle());
		dto.setKindName( (entity.getPracticeflg()==1?"練習用":"大会用") );
		
		return dto;
	}
	/**
	 * FORM→Entity
	 * @param form
	 * @return
	 */
	private QestionTblEntity getFrom(QuestionForm form) {
		Integer qid = form.getQid();
		QestionTblEntity entity = null;
		if( qid != null ) {
			entity = questionRepository.getOne(qid);
		}else {
			entity = new QestionTblEntity();
		}
		
		entity.setTitle(form.getTitle());
		entity.setSentence(form.getQuestion());
		entity.setPracticeflg( (form.getPracticeFlg() ? 1:0) );
		entity.setDifficalty(form.getDifficulty());
		entity.setCount(form.getQuestion().length());
		
		return entity;
	}
}
