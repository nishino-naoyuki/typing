package jp.ac.asojuku.typing.service;

import java.text.ParseException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.dto.ScoringResultDto;
import jp.ac.asojuku.typing.entity.AnsHistoryTblEntity;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventTblEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.exception.EventFinishedException;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.ScoringForm;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.repository.AnsTblRepository;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.service.scoring.AnswerSheet;
import jp.ac.asojuku.typing.service.scoring.Scoring;
import jp.ac.asojuku.typing.service.scoring.TypingAnswerSheet;
import jp.ac.asojuku.typing.service.scoring.TypingScoring;
import jp.ac.asojuku.typing.util.Exchange;

@Service
public class ScoringService extends ServiceBase{
	Logger logger = LoggerFactory.getLogger(ScoringService.class);

	/**
	 * 採点処理
	 * 
	 * @param uid
	 * @param scoringForm
	 * @return
	 * @throws SystemErrorException
	 * @throws EventFinishedException 
	 */
	@Transactional(rollbackFor = Exception.class)
	public ScoringResultDto typingScoring(Integer uid,ScoringForm scoringForm) throws SystemErrorException, EventFinishedException {
		
		if( !isInTime(scoringForm.getEid()) ) {
			throw new EventFinishedException("このイベントは既に終了しています");
		}
		ScoringResultDto result = null;
		try {
			//問題情報を取得（本当はフォームで送ったほうが良いが、通信のデータ量を考慮し、ここで最新のものを取得しておく）
			//なお、ここでは問題の閲覧権限があるかどうかは確認しない。権限はあるもの（ここ以前ではじいているという前提
			QestionTblEntity qEntity = questionRepository.getOne(scoringForm.getQid());
			
			AnswerSheet typingAnsSheet = new TypingAnswerSheet(
						qEntity.getSentence(),scoringForm.getAnswer(),
						scoringForm.getTime(),scoringForm.getKeycount()
					);
			
			Scoring scoring = new TypingScoring();
			
			//採点する
			result =  scoring.doScoring(typingAnsSheet);
			
			//DBに登録
			EventQuestionEntity eqEntity = eventQuestionRepository.findOne(
					Specification.
						where(EventQuestionSpecifications.qidEquals(scoringForm.getQid())).
						and(EventQuestionSpecifications.eidEquals(scoringForm.getEid()))
					).orElse(null);
			
			if( eqEntity != null ) {
				saveAnswer(scoringForm,result,eqEntity.getEqid(),uid);
			}
			
			//一時テーブルを削除
			ansTempTblRepository.deleteById( scoringForm.getToken() );
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new SystemErrorException(e.getMessage());
		}
		
		
		return result;
	}
	
	/**
	 * 時間ないかどうかの取得
	 * @param eid
	 * @return
	 */
	public boolean isInTime(Integer eid) {
		boolean inTime = true;
		
		EventTblEntity eventEntity = null;
		if( eid != null) {
			eventEntity = eventRepository.getOne(eid);
		}

		if( eventEntity == null ) {
			//eventEntityがnullは練習問題
			return true;
		}
		
		Date now = new Date();
		if( now.after( eventEntity.getFinishDate() ) ) {
			//終了時間を超えての採点
			inTime = false;
		}
		return inTime;
	}
	
	/**
	 * 解答情報を登録する
	 * 
	 * @param scoringForm
	 * @param result
	 * @param eqId
	 * @param uid
	 * @throws JsonProcessingException
	 * @throws ParseException
	 */
	private void saveAnswer(ScoringForm scoringForm, ScoringResultDto result, Integer eqId, Integer uid) throws JsonProcessingException, ParseException {
		int submitCount = 1;
		//既に回答があるか？
		AnsTblEntity ansTblEntity = ansTblRepository.findByUidAndEqid(uid,eqId);
		if( ansTblEntity != null ) {
			submitCount = ansTblEntity.getSubmitCount() + 1;
		}else {
			ansTblEntity = new AnsTblEntity();
			ansTblEntity.setEqid(eqId);
			ansTblEntity.setUid(uid);
		}
		ansTblEntity.setScore(result.getTotalScore());
		ansTblEntity.setSubmitCount(submitCount);
		
		ansTblEntity = ansTblRepository.save(ansTblEntity);
		
		//履歴データを追加
		AnsHistoryTblEntity ansHistory = new AnsHistoryTblEntity();
		
		ansHistory.setAnsid(ansTblEntity.getAnsid());
		ansHistory.setTime(scoringForm.getTime());
		ansHistory.setAnswer(scoringForm.getAnswer());
		ansHistory.setAnsTimestamp(
				Exchange.toDate(scoringForm.getSubmitYear(), scoringForm.getSubmitMounth(), scoringForm.getSubmitday(),
						scoringForm.getSubmitHour(), scoringForm.getSubmitMinutes(), scoringForm.getSubmitSecond()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(result);
		ansHistory.setSocreJson(jsonString);
		ansHistory.setCorrectFlg((result.isUnjustFlag() ? 1 : 0));
		ansHistory.setScore(result.getTotalScore());
		ansHistory.setSubmitNo(submitCount);
		
		ansHistoryTblRepository.save(ansHistory);
	}
}
