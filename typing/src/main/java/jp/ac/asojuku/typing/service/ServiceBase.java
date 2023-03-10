package jp.ac.asojuku.typing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.PersonalEventInfoDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsHistoryTblEntity;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.repository.AnsHistoryTblRepository;
import jp.ac.asojuku.typing.repository.AnsTblRepository;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.EventRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.util.Exchange;

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
	
	protected QuestionDetailDto getDetailForm(QestionTblEntity qEntity,Integer uid,Integer eid) {
		if( qEntity == null ) {
			return null;
		}
		QuestionDetailDto dto = getDetailForm(qEntity);
		//解答をセット
		AnsHistoryTblEntity ansEntity = ansHistoryTblRepository.getRecentlyOne(eid,qEntity.getQid(),uid);
		if( ansEntity != null ) {
			dto.setAnswer(ansEntity.getAnswer());
		}else {
			dto.setAnswer("");
		}
		
		return dto;		
	}

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
		return peiDto;
	}
}
