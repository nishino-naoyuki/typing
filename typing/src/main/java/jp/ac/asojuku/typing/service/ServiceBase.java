package jp.ac.asojuku.typing.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.PersonalEventInfoDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsHistoryTblEntity;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.AnsTempTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.param.TypingConst;
import jp.ac.asojuku.typing.repository.AnsDlTblRepository;
import jp.ac.asojuku.typing.repository.AnsHistoryTblRepository;
import jp.ac.asojuku.typing.repository.AnsTblRepository;
import jp.ac.asojuku.typing.repository.AnsTempTblRepository;
import jp.ac.asojuku.typing.repository.DownloadRepository;
import jp.ac.asojuku.typing.repository.EventDownloadRepository;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.EventRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.util.Exchange;
import jp.ac.asojuku.typing.util.Token;

public class ServiceBase {

	@Autowired
	protected QuestionRepository questionRepository;	
	@Autowired
	protected EventQuestionRepository eventQuestionRepository;
	@Autowired
	protected AnsTblRepository ansTblRepository;	
	@Autowired
	protected EventUserRepository eventUserRepository;
	@Autowired
	protected EventRepository eventRepository;
	@Autowired
	protected UserRepository userRepository;
	@Autowired
	protected AnsHistoryTblRepository ansHistoryTblRepository;
	@Autowired
	protected AnsTempTblRepository ansTempTblRepository;
	@Autowired
	protected DownloadRepository downloadRepository;
	@Autowired
	protected EventDownloadRepository eventDownloadRepository;
	@Autowired
	protected AnsDlTblRepository ansDlTblRepository;
	
	protected QuestionDetailDto getDetailForm(QestionTblEntity qEntity,Integer uid,Integer eid) {
		if( qEntity == null ) {
			return null;
		}
		//問題詳細を取得
		QuestionDetailDto dto = getDetailForm(qEntity);
		//一時テーブルデータを取得
		//一時テーブルにデータがあるかを取得する
		AnsTempTblEntity ansTempTblEntity = null;
		if( eid == TypingConst.PRACTICE_EVENTID ) {
			ansTempTblEntity = ansTempTblRepository.findByUidAndEidIsNullAndQid(uid, qEntity.getQid());
		}else {
			ansTempTblEntity = ansTempTblRepository.findByUidAndEidAndQid(uid, eid,qEntity.getQid());
		}
		if( ansTempTblEntity == null || ansTempTblEntity.getStartTime() == null) {
			dto.setStarted(false);
			String token;
			if( ansTempTblEntity == null ) {
				token = Token.getCsrfToken();
				insertOrUpdateAnsTempTblEntity(token,uid,(eid == TypingConst.PRACTICE_EVENTID?null:eid),qEntity.getQid(),null);
			}else {
				token = ansTempTblEntity.getToken();
			}
			dto.setToken(token);
		}else {
			dto.setStarted(true);
			dto.setToken(ansTempTblEntity.getToken());
			LocalDateTime ldt = Exchange.toLocalDateTime(ansTempTblEntity.getStartTime());
			dto.setStartYear(ldt.getYear());
			dto.setStartMonth(ldt.getMonthValue());
			dto.setStartDay(ldt.getDayOfMonth());
			dto.setStartHour(ldt.getHour());
			dto.setStartMinutes(ldt.getMinute());
			dto.setStartSecond(ldt.getSecond());
		}
		
		//解答をセット
		AnsHistoryTblEntity ansEntity = ansHistoryTblRepository.getRecentlyOne(eid,qEntity.getQid(),uid);
		if( ansEntity != null ) {
			dto.setAnswer(ansEntity.getAnswer());
		}else {
			dto.setAnswer("");
		}
		
		return dto;		
	}
	

	/**
	 * 問題文を取得する
	 * @param qEntity
	 * @return
	 */
	protected QuestionDetailDto getDetailForm(QestionTblEntity qEntity) {
		if( qEntity == null ) {
			return null;
		}
		QuestionDetailDto dto = new QuestionDetailDto();
		
		dto.setQid(qEntity.getQid());
		dto.setSentence(qEntity.getSentence());
		dto.setTitle(qEntity.getTitle());
		dto.setDifficulty(qEntity.getDifficalty());
		dto.setPracticeFlg(qEntity.getPracticeflg()==1?true:false);
		//どの大会に使われているか？
		List<EventQuestionEntity> eqList = eventQuestionRepository.findAll(
				Specification
				.where( EventQuestionSpecifications.qidEquals(qEntity.getQid()) ),
				Sort.by(Sort.Direction.ASC,"no"));
		
		List<EventOutlineDto> eventList = new ArrayList<>();
		for(EventQuestionEntity eqEntity :eqList) {
			EventOutlineDto eventDto = new EventOutlineDto();
			if( eqEntity.getEventTbl() != null ) {
				eventDto.setEid(eqEntity.getEventTbl().getEid());
				eventDto.setName(eqEntity.getEventTbl().getName());
			}else {
				eventDto.setEid(null);
				eventDto.setName("練習用");
			}
			eventList.add(eventDto);
		}
		dto.setEventList(eventList);		
		
		return dto;		
	}

	/**
	 * 個人成績を取得する
	 * @param eid
	 * @param uid
	 * @return
	 */
	public PersonalEventInfoDto getPersonalEventInfo(Integer eid,Integer uid) {
		PersonalEventInfoDto peiDto = new PersonalEventInfoDto();
		////////////////////////
		//ランキングを取得する
		List<RankingSummary> rankingSummaryList = ansTblRepository.findRankingSummary(eid);
		int ranking = 1;
		int count = 1;
		int wkScore = Integer.MAX_VALUE;
		for(RankingSummary summary : rankingSummaryList) {
			if( wkScore > summary.getScore()) {
				ranking = count;
			}
			if(summary.getUid() == uid) {
				peiDto.setRank(ranking);
				peiDto.setTotalScore(summary.getScore());
				Integer submitCount = ansTblRepository.getAnsCountByEidUid(eid, uid);
				peiDto.setSubmitCount((submitCount == null ? 0 : submitCount) );
				peiDto.setGetTime(Exchange.toFormatString(new Date()));
				peiDto.setEid(eid);
				peiDto.setEventName(eventRepository.getOne(eid).getName()); 
				break;
			}
			count++;
			wkScore = summary.getScore();
		}
		/////////////////////////////
		//個別の問題一覧を取得
		List<QuestionOutlineDto> qList = new ArrayList<>();
		List<EventQuestionEntity> eqEntityList =  eventQuestionRepository.findByEidOrderByNo(eid);
		for( EventQuestionEntity eqEntity : eqEntityList) {
			QuestionOutlineDto qDto = getQuestionOutlineDtoForUser(eqEntity,uid);				
			qList.add(qDto);
		}
		peiDto.setQList(qList);
		
		
		return peiDto;
	}
	/** protected **/

	/**
	 * ユーザー個別のスコア月問第一案を取得する
	 * @param eqEntity
	 * @param uid
	 * @return
	 */
	protected QuestionOutlineDto getQuestionOutlineDtoForUser(EventQuestionEntity eqEntity,Integer uid) {

		QuestionOutlineDto qDto = new QuestionOutlineDto();
		//全てはセットしない
		Integer qid = eqEntity.getQestionTbl().getQid();
		qDto.setQid(qid);
		qDto.setEqid(eqEntity.getEqid());
		qDto.setNo(eqEntity.getNo());
		qDto.setDifficulty(eqEntity.getQestionTbl().getDifficalty());
		qDto.setPracticeFlg(eqEntity.getQestionTbl().getPracticeflg());
		qDto.setTitle(eqEntity.getQestionTbl().getTitle());
		AnsTblEntity ansEntity = ansTblRepository.findByUidAndEqid(uid, eqEntity.getEqid());
		if( ansEntity != null ) {
			qDto.setScore( String.valueOf(ansEntity.getScore()) );
		}else {
			qDto.setScore("未解答");
		}
		
		return qDto;
	}
	
	/**
	 * 一時テーブルを挿入または更新する
	 * @param token
	 * @param uid
	 * @param eqid
	 * @param ldt
	 */
	private void insertOrUpdateAnsTempTblEntity(String token,Integer uid,Integer eid,Integer qid,LocalDateTime ldt) {
		AnsTempTblEntity entity = new AnsTempTblEntity();
		
		entity.setToken(token);
		entity.setUid(uid);
		entity.setQid(qid);
		entity.setEid(eid);
		entity.setStartTime( Exchange.toDate(ldt) );
		
		ansTempTblRepository.save(entity);
	}
}
