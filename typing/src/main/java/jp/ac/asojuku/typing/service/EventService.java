package jp.ac.asojuku.typing.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.entity.EventTblEntity;
import jp.ac.asojuku.typing.param.EventState;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.repository.EventRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;

@Service
public class EventService {
	Logger logger = LoggerFactory.getLogger(EventService.class);
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EventUserRepository eventUserRepository;
	/**
	 * イベントの一覧を表示する
	 * 学生は公開後でかつフィルターを通る情報、管理者は全ての情報が取れる
	 * @param mail
	 * @param roleId
	 * @return
	 */
	public List<EventOutlineDto> getList(Integer uid,String mail,RoleId roleId) {
		Date now = new Date();
		
		List<EventTblEntity> eventEntityList=null;
		if( roleId == RoleId.STUDENT) {
			eventEntityList = eventRepository.findByPublicDateLessThanEqualOrderByStartDateDesc(now);
		}else {
			eventEntityList = eventRepository.findAllByOrderByPublicDateDesc();
		}
		
		List<EventOutlineDto> dtoList = new ArrayList<> ();
		//フィルタリング
		for( EventTblEntity entity : eventEntityList) {
			if( roleId == RoleId.ADMIN || mail.matches(entity.getFilter())) {
				dtoList.add(getFrom(uid,entity,now,roleId));
			}
		}
		return dtoList;
	}
	
	/**
	 * Entity→DTO変換
	 * @param entity
	 * @param now
	 * @return
	 */
	private EventOutlineDto getFrom(Integer uid,EventTblEntity entity,Date now,RoleId roleId) {
		EventOutlineDto dto = new EventOutlineDto();
		
		dto.setEId( entity.getEid() );
		dto.setName( entity.getName() );
		dto.setPublicDateTime( entity.getPublicDate() );
		dto.setStartDateTime( entity.getStartDate() );
		dto.setEndDateTime( entity.getFinishDate() );
		dto.setOwnerName( entity.getOwerName() );
		
		//状態を設定する
		if( now.before(entity.getPublicDate())) {
			//公開日以前
			dto.setEventState(EventState.PREPUBLIC);
		}else if( now.before(entity.getStartDate())) {
			//開催日以前
			dto.setEventState(EventState.PRE);
		}else if( now.after(entity.getFinishDate())) {
			//終了日以後
			dto.setEventState(EventState.AFTER);
		}else {
			dto.setEventState(EventState.INPROGRESS);
		}
		
		//参加状況をセットする
		if( roleId == RoleId.STUDENT ) {
			if( eventUserRepository.findByUidAndEid(uid, entity.getEid()) != null) {
				dto.setJoinFlag(true);
			}else {
				dto.setJoinFlag(false);
			}
			
		}else {
			dto.setJoinFlag(false);
		}
		
		return dto;
	}
}
