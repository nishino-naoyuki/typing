package jp.ac.asojuku.typing.service;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.dto.ScoringResultDto;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
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
	 */
	@Transactional(rollbackFor = Exception.class)
	public ScoringResultDto typingScoring(Integer uid,ScoringForm scoringForm) throws SystemErrorException {
		
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
			
			//学生の場合はDBに登録
			EventQuestionEntity eqEntity = eventQuestionRepository.findOne(
					Specification.
						where(EventQuestionSpecifications.qidEquals(scoringForm.getQid())).
						and(EventQuestionSpecifications.eidEquals(scoringForm.getEid()))
					).orElse(null);
			
			if( eqEntity != null ) {
				AnsTblEntity ansEntity = getFrom(scoringForm,result,eqEntity.getEqid(),uid);
				
				ansTblRepository.save(ansEntity);
			}
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new SystemErrorException(e.getMessage());
		}
		
		
		return result;
	}
	
	/**
	 * Entityを作成する
	 * @param scoringForm
	 * @param result
	 * @param eqId
	 * @param uid
	 * @return
	 * @throws ParseException
	 * @throws JsonProcessingException
	 */
	private AnsTblEntity getFrom(ScoringForm scoringForm, ScoringResultDto result, Integer eqId, Integer uid)
			throws ParseException, JsonProcessingException {
		AnsTblEntity ansEntity = new AnsTblEntity();

		ansEntity.setCorrectFlg((result.isUnjustFlag() ? 1 : 0));
		ansEntity.setUid(uid);
		ansEntity.setEqid(eqId);
		ansEntity.setScore(result.getTotalScore());
		ansEntity.setTime(scoringForm.getTime());
		ansEntity.setAnswer(scoringForm.getAnswer());
		ansEntity.setAnsTimestamp(
				Exchange.toDate(scoringForm.getSubmitYear(), scoringForm.getSubmitMounth(), scoringForm.getSubmitday(),
						scoringForm.getSubmitHour(), scoringForm.getSubmitMinutes(), scoringForm.getSubmitSecond()));
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(result);
		ansEntity.setLog(jsonString);

		return ansEntity;
	}
}
