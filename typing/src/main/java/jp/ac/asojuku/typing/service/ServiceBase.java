package jp.ac.asojuku.typing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.repository.AnsTblRepository;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.EventRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;

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
	
	protected QuestionDetailDto getDetailForm(QestionTblEntity qEntity,Integer uid) {
		if( qEntity == null ) {
			return null;
		}
		QuestionDetailDto dto = new QuestionDetailDto();
		
		dto.setQid(qEntity.getQid());
		dto.setSentence(qEntity.getSentence());
		dto.setTitle(qEntity.getTitle());
		dto.setDifficulty(qEntity.getDifficalty());
		//解答をセット
		AnsTblEntity ansEntity = ansTblRepository.getRecentlyOne(qEntity.getQid(),uid);
		if( ansEntity != null ) {
			dto.setAnswer(ansEntity.getAnswer());
		}else {
			dto.setAnswer("");
		}
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
}
