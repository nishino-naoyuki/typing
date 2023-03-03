package jp.ac.asojuku.typing.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.dto.EventInfoDto;
import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.dto.UserInfoDto;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventTblEntity;
import jp.ac.asojuku.typing.entity.EventUserEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.EventCreateForm;
import jp.ac.asojuku.typing.param.EventState;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.EventRepository;
import jp.ac.asojuku.typing.repository.EventUserRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.util.Exchange;
import jp.ac.asojuku.typing.util.Token;

@Service
public class EventService {
	Logger logger = LoggerFactory.getLogger(EventService.class);
	private final String DATE_FMT = "yyyy-MM-dd'T'HH:mm";
	private final Integer RANKING_DISP = 1;
	
	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	EventUserRepository eventUserRepository;
	
	@Autowired
	EventQuestionRepository eventQuestionRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	/**
	 * イベントの詳細情報を取得する
	 * @param eid
	 * @param roleId
	 * @return
	 */
	public EventInfoDto getDetail(Integer eid,RoleId roleId) {
		
		EventTblEntity entity = eventRepository.getOne(eid);
		
		List<EventQuestionEntity> eqEntityList =  eventQuestionRepository.findByEidOrderByNo(eid);
		
		List<EventUserEntity> euEntityList = eventUserRepository.findByEidOrderByUid(eid);

		
		return getFrom(entity,eqEntityList,euEntityList,roleId);
	}
	/**
	 * 指定のユーザーが指定のイベントに既に登録されているかを取得
	 * @param uid
	 * @param eid
	 * @return
	 */
	public boolean isExistEventUser(Integer uid,Integer eid) {
		return eventUserRepository.findByUidAndEid(uid, eid) != null;
	}
	/**
	 * イベントの一覧を表示する
	 * 学生は公開後でかつフィルターを通る情報、管理者は全ての情報が取れる
	 * @param mail
	 * @param roleId
	 * @return
	 */
	public List<EventOutlineDto> getList(Integer uid,String mail,RoleId roleId) {
		Date now = new Date();
		
		logger.info(now.toString());
		List<EventTblEntity> eventEntityList=null;
		if( roleId == RoleId.STUDENT) {
			//名前なが～　公開日の範囲内で、イベント開始時の降順で取得
			eventEntityList = 
					eventRepository.findByPublicDateLessThanEqualAndPublicEndDateGreaterThanEqualOrderByStartDateDesc(now,now);
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
	 * 挿入
	 * @param eventCreateForm
	 * @throws SystemErrorException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insert(EventCreateForm eventCreateForm) throws SystemErrorException {
		EventTblEntity entity = null;
		
		try {
			//イベント登録
			entity = eventRepository.save(getFrom(eventCreateForm));
			
			//問題の登録いったん削除し作り直し
			insertQuestions(entity.getEid(),eventCreateForm);
			
		}catch(ParseException e) {
			logger.warn(e.getMessage());
			throw new SystemErrorException(e.getMessage());
		}
	}
	
	
	/* ----prvate---- */
	private EventInfoDto getFrom(
			EventTblEntity entity,
			List<EventQuestionEntity> eqEntityList,
			List<EventUserEntity> euEntityList,
			RoleId roleId) {
		EventInfoDto dto = new EventInfoDto();
		
		dto.setEid(entity.getEid());
		dto.setName(entity.getName());
		dto.setStartDateTime(entity.getStartDate());
		dto.setEndDateTime(entity.getFinishDate());
		
		Date now = new Date();		
		//ランキングを表示するかどうかを確認する
		if( entity.getRankingdisplay() == RANKING_DISP) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(entity.getFinishDate());
			calendar.add(Calendar.MINUTE, -entity.getHiderankingtime());
			if( now.before(calendar.getTime())) {
				//表示する設定で時間内
				dto.setRankingDisplay(true);
			}else {
				//表示する設定で時間外
				dto.setRankingDisplay(false);
			}
		}else {
			//表示しない設定
			dto.setRankingDisplay(false);
		}
		
		//問題リストをセット
		List<QuestionOutlineDto> qList = new ArrayList<>();
		if( roleId == RoleId.ADMIN || 
		   (roleId == RoleId.STUDENT && now.after(entity.getStartDate()))) {
			for( EventQuestionEntity eqEntity : eqEntityList) {
				QuestionOutlineDto qDto = new QuestionOutlineDto();
				//全てはセットしない
				qDto.setQid(eqEntity.getQestionTbl().getQid());
				qDto.setDifficulty(eqEntity.getQestionTbl().getDifficalty());
				qDto.setPracticeFlg(eqEntity.getQestionTbl().getPracticeflg());
				qDto.setTitle(eqEntity.getQestionTbl().getTitle());
			}
		}
		dto.setQList(qList);
		
		//ユーザーリスト
		List<UserInfoDto> uList = new ArrayList<>();
		if( roleId == RoleId.ADMIN ) {
			for( EventUserEntity euEntity : euEntityList) {
				UserInfoDto uDto = new UserInfoDto();
				//全てはセットしない
				uDto.setName(euEntity.getUserTbl().getName());
				uDto.setMail(euEntity.getUserTbl().getMail());
				uDto.setDispName(euEntity.getUserTbl().getDispName());
				uDto.setAffiliation(euEntity.getUserTbl().getAffiliation());
				uDto.setUid(euEntity.getUserTbl().getUid());
			}
		}
		dto.setUList(uList);
		
		return dto;
	}
	/**
	 * イベントに問題を追加する
	 * @param eid
	 * @param eventCreateForm
	 */
	private void insertQuestions(Integer eid,EventCreateForm eventCreateForm) {
		//いったん削除
		eventQuestionRepository.deleteByEid(eid);

		//追加する
		int no = 1;
		List<EventQuestionEntity> entityList = new ArrayList<>();
		for(Integer qid: eventCreateForm.getQidList() ) {
			EventQuestionEntity eqEntity = new EventQuestionEntity();
			
			eqEntity.setEid(eid);
			eqEntity.setNo(no);
			eqEntity.setQid(qid);
			
			entityList.add(eqEntity);
			no++;
		}
		
		if( entityList.size() > 0 ) {
			eventQuestionRepository.saveAll(entityList);
		}
	}
	
	/**
	 * Form→Entityの変換
	 * @param eventCreateForm
	 * @return
	 * @throws ParseException
	 */
	private EventTblEntity getFrom(EventCreateForm eventCreateForm) throws ParseException {
		EventTblEntity entity = new EventTblEntity();
	
		entity.setName( eventCreateForm.getEventname() );
		entity.setFilter( ( StringUtils.isEmpty( eventCreateForm.getFilter() ) ? ".*": eventCreateForm.getFilter()) );
		entity.setOwerName( eventCreateForm.getOwnername() );
		entity.setRankingdisplay( eventCreateForm.getRankingdisplay() );
		entity.setHiderankingtime( eventCreateForm.getHiderankingtime() );
		entity.setPublicDate( Exchange.toDate(eventCreateForm.getPublicstartdatetime(),DATE_FMT) );
		entity.setStartDate( Exchange.toDate(eventCreateForm.getStartdatetime(),DATE_FMT) );
		entity.setFinishDate( Exchange.toDate(eventCreateForm.getEnddatetime(),DATE_FMT) );
		entity.setPublicEndDate( Exchange.toDate(eventCreateForm.getPublicenddatetime(),DATE_FMT) );
		
		return entity;
	}
	/**
	 * Entity→DTO変換
	 * @param entity
	 * @param now
	 * @return
	 */
	private EventOutlineDto getFrom(Integer uid,EventTblEntity entity,Date now,RoleId roleId) {
		EventOutlineDto dto = new EventOutlineDto();
		
		dto.setEid( entity.getEid() );
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
		
		//トークンを発行する（コントローラー側でトークンとイベントIDをセッションイン保存しておく）
		dto.setToken(Token.getCsrfToken());
		
		return dto;
	}
}
