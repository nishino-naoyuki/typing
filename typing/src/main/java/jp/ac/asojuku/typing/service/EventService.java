package jp.ac.asojuku.typing.service;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.config.SystemConfig;
import jp.ac.asojuku.typing.dto.DownloadQFileDto;
import jp.ac.asojuku.typing.dto.EventInfoDetailDto;
import jp.ac.asojuku.typing.dto.EventInfoDto;
import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.PersonalEventInfoDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.dto.RankingDto;
import jp.ac.asojuku.typing.dto.UserInfoDto;
import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.DownloadTblEntity;
import jp.ac.asojuku.typing.entity.EventDownloadEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventTblEntity;
import jp.ac.asojuku.typing.entity.EventUserEntity;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.EventCreateForm;
import jp.ac.asojuku.typing.param.EventState;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.util.Exchange;
import jp.ac.asojuku.typing.util.FileUtils;
import jp.ac.asojuku.typing.util.Token;

@Service
public class EventService extends ServiceBase{
	Logger logger = LoggerFactory.getLogger(EventService.class);
	private final String DATE_FMT = "yyyy-MM-dd'T'HH:mm";
	private final Integer RANKING_DISP = 1;
		
	/**
	 * ランキングが表示可能かどうかを返す
	 * @param role
	 * @param eid
	 * @return
	 */
	public boolean isRankingDisplay(RoleId role,Integer eid) {
		EventTblEntity entity = eventRepository.getOne(eid);
		boolean isDisp = false;
		
		//管理者は常に見れる
		if( role == RoleId.ADMIN ) {
			return true;
		}
		
		Date now = new Date();		
		//ランキングを表示するかどうかを確認する
		if( entity.getRankingdisplay() == RANKING_DISP) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(entity.getFinishDate());
			calendar.add(Calendar.MINUTE, -entity.getHiderankingtime());
			if( now.before(calendar.getTime())) {
				//表示する設定で時間内
				isDisp = true;
			}
		}
		
		return isDisp;
	}
	/**
	 * ランキングを取得する
	 * @param eid
	 * @return
	 */
	public List<RankingDto> getRankingAll(Integer eid){
		List<RankingSummary> rankingSummaryList = ansTblRepository.findRankingSummary(eid);
		
		List<RankingDto> rankingList = new ArrayList<>();
		int ranking = 1;
		int count = 1;
		int wkScore = Integer.MAX_VALUE;
		for(RankingSummary summary : rankingSummaryList) {
			RankingDto rDto = new RankingDto();
			if( wkScore > summary.getScore()) {
				ranking = count;
			}
			UserTblEntity userEntity = userRepository.getOne(summary.getUid());
			rDto.setUid(summary.getUid());
			rDto.setScore(summary.getScore());
			rDto.setRanking(ranking);
			rDto.setName(userEntity.getName());
			rDto.setDispName(userEntity.getDispName());
			rDto.setMail(userEntity.getMail());
			rDto.setAffiliation(userEntity.getAffiliation());
			
			rankingList.add(rDto);
			count++;
			wkScore = summary.getScore();
		}
		
		return rankingList;
	}
	/**
	 * 編集用のイベント情報詳細を取得する
	 * @param eid
	 * @return
	 */
	public EventInfoDetailDto getDetail(Integer eid) {
		EventTblEntity entity = eventRepository.getOne(eid);
		
		EventInfoDetailDto dto = new EventInfoDetailDto();

		dto.setEid(entity.getEid());
		dto.setEventname(entity.getName());
		dto.setPublicstartdatetime(entity.getPublicDate());
		dto.setPublicenddatetime(entity.getPublicEndDate());
		dto.setStartdatetime(entity.getStartDate());
		dto.setEnddatetime(entity.getFinishDate());
		dto.setOwnername(entity.getOwerName());
		dto.setRankingdisplay( entity.getRankingdisplay()==1?true:false );
		dto.setHiderankingtime( entity.getHiderankingtime() );
		dto.setFilter( entity.getFilter() );
		
		//問題リストを取得
		List<EventQuestionEntity> eqEntityList =  eventQuestionRepository.findByEidOrderByNo(eid);
		List<QuestionOutlineDto> qList = new ArrayList<>();
		for( EventQuestionEntity eqEntity : eqEntityList) {
			QuestionOutlineDto qDto = new QuestionOutlineDto();
			//全てはセットしない
			Integer qid = eqEntity.getQestionTbl().getQid();
			qDto.setQid(qid);
			qDto.setEqid(eqEntity.getEqid());
			qDto.setNo(eqEntity.getNo());
			qDto.setDifficulty(eqEntity.getQestionTbl().getDifficalty());
			qDto.setPracticeFlg(eqEntity.getQestionTbl().getPracticeflg());
			qDto.setTitle(eqEntity.getQestionTbl().getTitle());
			
			qList.add(qDto);
		}
		dto.setQList(qList);
		
		//ダウンロード問題を取得
		List<EventDownloadEntity> edEntityList =  eventDownloadRepository.findByEidOrderByNo(eid);
		List<DownloadQFileDto> downloadqFileList = new ArrayList<>();
		for( EventDownloadEntity edEntity : edEntityList ) {
			DownloadQFileDto dlDto = new DownloadQFileDto(
					edEntity.getDownloadTbl().getDownloadId(),
					FileUtils.getFileNameFromPath( edEntity.getDownloadTbl().getFilename() )
					);
			downloadqFileList.add(dlDto);
		}
		dto.setDownloadqFileList(downloadqFileList);
		
		return dto;
	}
	/**
	 * イベントの詳細情報を取得する
	 * @param eid
	 * @param roleId
	 * @return
	 */
	public EventInfoDto getDetail(Integer eid,Integer uid,RoleId roleId) {
		
		EventTblEntity entity = eventRepository.getOne(eid);
		
		if( roleId == RoleId.STUDENT) {
			//学生の場合は表示可能かどうかを家訓する
			UserTblEntity userEntity = userRepository.getOne(uid);
			if( !userEntity.getMail().matches( entity.getFilter() )) {
				//表示権限がない場合
				return null;
			}
		}
		
		List<EventQuestionEntity> eqEntityList =  eventQuestionRepository.findByEidOrderByNo(eid);
		
		List<EventUserEntity> euEntityList = eventUserRepository.findByEidOrderByUid(eid);

		
		return getFrom(entity,eqEntityList,euEntityList,uid,roleId);
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
			String filter = entity.getFilter();
			if( roleId == RoleId.ADMIN || mail.matches(filter)) {
				dtoList.add(getFrom(uid,entity,now,roleId));
			}
		}
		return dtoList;
	}

	/**
	 * 挿入・更新
	 * @param eventCreateForm
	 * @throws SystemErrorException
	 */
	@Transactional(rollbackFor = Exception.class)
	public void save(EventCreateForm eventCreateForm,List<DownloadQFileDto> uploadFileList) throws SystemErrorException {
		EventTblEntity entity = null;
		
		try {
			//イベント登録
			entity = eventRepository.save(getFrom(eventCreateForm));
			
			//問題の登録いったん削除し作り直し
			insertQuestions(entity.getEid(),eventCreateForm);
			
			//アップロード問題を登録する
			if( uploadFileList.size() > 0 ) {
				int no = 1;
				List<DownloadTblEntity> dlList = new ArrayList<>();
				for(DownloadQFileDto uploadqFile : uploadFileList ) {
					DownloadTblEntity dlEntty =
								downloadRepository.save( getFrom( uploadqFile) );
					dlList.add(dlEntty);
					insertUpdateEventDownload(entity.getEid(),dlEntty.getDownloadId(),no);
					no++;
				}
				
				//問題（連関エンティティ）の削除
				deleteEventDownload(entity.getEid(),dlList);
				
			}
			
			
		}catch(ParseException e) {
			logger.warn(e.getMessage());
			throw new SystemErrorException(e.getMessage());
		}
	}

	/**
	 * 大会に登録されている問題を取得する
	 * 
	 * @param eqid
	 * @return
	 */
	public QuestionDetailDto getQuestion(Integer uid,RoleId role,Integer eqid) {
		if(role == RoleId.STUDENT && !isDisplay(uid,eqid)) {
			//表示不可能な場合はnullを返す
			return null;
		}
		
		EventQuestionEntity eqEntity = eventQuestionRepository.getOne(eqid);
		QuestionDetailDto detailDto =  getDetailForm(  eqEntity.getQestionTbl(),uid,eqEntity.getEid() );
		detailDto.setEid(eqEntity.getEid());
		
		return detailDto;
	}
	
	/**
	 * 指定したユーザーと大会の問題が表示可能かを返す
	 * @param uid
	 * @param eqid
	 * @return
	 */
	public boolean isDisplay(Integer uid,Integer eqid) {
		
		EventQuestionEntity eqEntity = eventQuestionRepository.getOne(eqid);
		EventTblEntity eventEntity = eqEntity.getEventTbl();
		Date now = new Date();
		if( eventEntity.getStartDate().after(now) || eventEntity.getFinishDate().before(now)) {
			//開始前、終了後は見れない
			return false;
		}
		EventUserEntity euEntity =  eventUserRepository.findByUidAndEid(uid, eventEntity.getEid());
		if( euEntity == null ) {
			//そのユーザーは登録していない
			return false;
		}
		
		return true;
	}
	
	
	/* ----prvate---- */
	
	private EventInfoDto getFrom(
			EventTblEntity entity,
			List<EventQuestionEntity> eqEntityList,
			List<EventUserEntity> euEntityList,
			Integer uid,
			RoleId roleId) {
		EventInfoDto dto = new EventInfoDto();
		
		dto.setEid(entity.getEid());
		dto.setName(entity.getName());
		dto.setStartDateTime(entity.getStartDate());
		dto.setEndDateTime(entity.getFinishDate());
		//ランキングを表示するかどうかを確認する
		dto.setRankingDisplay(isRankingDisplay(roleId,entity.getEid()));
		Date now = new Date();		
		
		//終了済みかどうかをセットする
		if( roleId == RoleId.ADMIN ||
			(roleId == RoleId.STUDENT && now.before(entity.getFinishDate()))) {
			dto.setPastEvent(false);
		}else {
			dto.setPastEvent(true);
		}
		//開始前かどうかをセットする
		if( roleId == RoleId.ADMIN ||
			(roleId == RoleId.STUDENT && now.after(entity.getStartDate()))) {
			dto.setBeforeEvent(false);
		}else {
			dto.setBeforeEvent(true);
		}

		//ユーザーが学生の場合は登録済みかどうかを確認する
		boolean entryFlag = true;
		if(roleId == RoleId.STUDENT) {
			entryFlag = isExistEventUser(uid,entity.getEid());
		}
		dto.setEntryFlag(entryFlag);
		
		//問題リストをセット
		//先生：無条件でリストを表示する
		//学生：公開期限内でそのユーザーがイベントに登録済みの場合リストを表示する
		List<QuestionOutlineDto> qList = new ArrayList<>();
		if( roleId == RoleId.ADMIN || 
		   (roleId == RoleId.STUDENT && now.after(entity.getStartDate()) && now.before(entity.getFinishDate()) && entryFlag )
		   ) {
			dto.setDisplayQuestion(true);
			for( EventQuestionEntity eqEntity : eqEntityList) {
				QuestionOutlineDto qDto = getQuestionOutlineDtoForUser(eqEntity,uid);				
				qList.add(qDto);
			}
		}else {
			dto.setDisplayQuestion(false);
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
				uList.add(uDto);
			}
		}
		dto.setUList(uList);
		
		long deff = Exchange.differenceDate(entity.getStartDate(),new Date());
		dto.setLefttime((deff < 0 ? 0:deff));
		
		return dto;
	}
	
	/**
	 * イベントに問題を追加する
	 * @param eid
	 * @param eventCreateForm
	 */
	private void insertQuestions(Integer eid,EventCreateForm eventCreateForm) {

		//現状を取得する
		List<EventQuestionEntity> eqList = eventQuestionRepository.findByEidOrderByNo(eid);

		//追加・更新
		insertOrUpdateEventQuestion(eid,eventCreateForm,eqList);
		
		//現状のリストにあって、新リストに無いものは削除する
		deteleEventQuestion(eventCreateForm,eqList);
		
	}
	
	/**
	*既に解答済みのものを考慮して、全削除→全追加はしない（外部キーの制エラーもある）
	*現状のものと突き合わせて、既にあれば更新、無ければ追加、消えていれば削除を行う
	 * @param eventCreateForm
	 * @param eqList
	 */
	private void insertOrUpdateEventQuestion(
			Integer eid,EventCreateForm eventCreateForm,List<EventQuestionEntity> eqList) {

		//既に解答済みのものを考慮して、全削除→全追加はしない（外部キーの制エラーもある）
		//現状のものと突き合わせて、既にあれば更新、無ければ追加、消えていれば削除を行う
		int no = 1;
		for(Integer qid: eventCreateForm.getQidList() ) {
			//現状存在するかを確認
			EventQuestionEntity eqEntity = new EventQuestionEntity();
			for(EventQuestionEntity eqElement : eqList) {
				if( eqElement.getQid() == qid ) {
					//存在する場合は更新するためにキーをセットしておく
					eqEntity.setEqid(eqElement.getEqid());
					break;
				}
			}
			eqEntity.setEid(eid);
			eqEntity.setNo(no);
			eqEntity.setQid(qid);
			eventQuestionRepository.save(eqEntity);
			no++;
		}
	}
	
	/**
	 * 現状のリストにあって、新リストに無いものは削除する
	 * @param eventCreateForm
	 * @param eqList
	 */
	private void deteleEventQuestion(EventCreateForm eventCreateForm,List<EventQuestionEntity> eqList) {

		for(EventQuestionEntity eqElement : eqList ) {
			boolean bFindFlg = false;
			//新リストにあるか？
			for(Integer qid: eventCreateForm.getQidList()){
				if( qid == eqElement.getQid()) {
					bFindFlg = true;
					break;
				}
			}
			if( !bFindFlg ) {
				//解答履歴を削除
				ansHistoryTblRepository.delteByEqId(eqElement.getEqid());
				//解答情報を削除する
				ansTblRepository.delteByEqId(eqElement.getEqid());
				//リストから削除する
				eventQuestionRepository.deleteById(eqElement.getEqid());
			}
		}
	}
	/**
	 * Form→Entityの変換
	 * @param eventCreateForm
	 * @return
	 * @throws ParseException
	 */
	private EventTblEntity getFrom(EventCreateForm eventCreateForm) throws ParseException {
		Integer eid = eventCreateForm.getEid();
		EventTblEntity entity = null;
		if( eid != null ) {
			entity = eventRepository.getOne(eid);
		}else {
			entity = new EventTblEntity();
		}
	
		entity.setName( eventCreateForm.getEventname() );
		entity.setFilter( ( StringUtils.isEmpty( eventCreateForm.getFilter() ) ? ".*": eventCreateForm.getFilter()) );
		entity.setOwerName( eventCreateForm.getOwnername() );
		entity.setRankingdisplay( eventCreateForm.getRankingdisplay() );
		entity.setHiderankingtime( eventCreateForm.getHiderankingtime()==null ? 0: eventCreateForm.getHiderankingtime()  );
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
	
	/**
	 * 
	 * @param ordinalNum
	 * @param uploadqFile
	 * @return
	 */
	private DownloadTblEntity getFrom(DownloadQFileDto uploadqFile) {
		
		DownloadTblEntity entity = null;
		if( uploadqFile.getUploadfileId() == null ) {
			entity = new DownloadTblEntity();
		}else {
			entity = downloadRepository.getOne(uploadqFile.getUploadfileId());
		}
		
		String baseDir = SystemConfig.getInstance().getExcelbasedir();
		
		entity.setFilename(uploadqFile.getUploadfile().replace(baseDir, ""));
		
		return entity;
	}
	
	/**
	 * 
	 * @param eId
	 * @param dlId
	 */
	private void insertUpdateEventDownload(int eId,int dlId,int no) {
		EventDownloadEntity edlEntity = 
					eventDownloadRepository.findByEidAndDownloadIdAndNo(eId,dlId,no);
		
		if( edlEntity == null ) {
			edlEntity = new EventDownloadEntity();
			edlEntity.setEid(eId);
			edlEntity.setDownloadId(dlId);
			edlEntity.setNo(no);
			eventDownloadRepository.save(edlEntity);
		}
	}
	
	private void deleteEventDownload(int eId,List<DownloadTblEntity> dlList) {
		List<EventDownloadEntity> edList = eventDownloadRepository.findByEidOrderByNo(eId);
		
		for(EventDownloadEntity edlEntity : edList ) {
			boolean findFlag = false;
			for(DownloadTblEntity dlEntity : dlList) {
				if( edlEntity.getDownloadId() == dlEntity.getDownloadId() ) {
					findFlag = true;
					break;
				}
			}
			//元データにあって、新データに無い場合は削除
			if( !findFlag ) {
				//DB削除
				eventDownloadRepository.delete(edlEntity);
				//ファイル削除
				deleteDownloadFile(edlEntity);
			}
		}
	}
	
	/**
	 * 
	 * @param edlEntity
	 */
	private void deleteDownloadFile(EventDownloadEntity edlEntity) {
		String baseDir = SystemConfig.getInstance().getExcelbasedir();
		String filePath = baseDir + edlEntity.getDownloadTbl().getFilename();
		
		FileUtils.delete(filePath);
	}
}
