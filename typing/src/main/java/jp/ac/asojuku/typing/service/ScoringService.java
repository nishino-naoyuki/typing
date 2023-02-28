package jp.ac.asojuku.typing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.dto.ScoringResultDto;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.form.ScoringForm;
import jp.ac.asojuku.typing.repository.EventQuestionRepository;
import jp.ac.asojuku.typing.repository.QuestionRepository;
import jp.ac.asojuku.typing.repository.specifications.EventQuestionSpecifications;
import jp.ac.asojuku.typing.service.scoring.AnswerSheet;
import jp.ac.asojuku.typing.service.scoring.Scoring;
import jp.ac.asojuku.typing.service.scoring.TypingAnswerSheet;
import jp.ac.asojuku.typing.service.scoring.TypingScoring;

@Service
public class ScoringService {
	@Autowired
	QuestionRepository questionRepository;
	
	@Autowired
	EventQuestionRepository eventQuestionRepository;

	@Transactional(rollbackFor = Exception.class)
	public ScoringResultDto typingScoring(Integer uid,ScoringForm scoringForm) {
		
		//問題情報を取得（本当はフォームで送ったほうが良いが、通信のデータ量を考慮し、ここで最新のものを取得しておく）
		//なお、ここでは問題の閲覧権限があるかどうかは確認しない。権限はあるもの（ここ以前ではじいているという前提
		QestionTblEntity qEntity = questionRepository.getOne(scoringForm.getQid());
		
		AnswerSheet typingAnsSheet = new TypingAnswerSheet(
					qEntity.getSentence(),scoringForm.getAnswer(),
					scoringForm.getTime(),scoringForm.getKeycount()
				);
		
		Scoring scoring = new TypingScoring();
		
		//採点する
		ScoringResultDto result =  scoring.doScoring(typingAnsSheet);
		
		//DBに登録
		EventQuestionEntity eqEntity = eventQuestionRepository.findOne(
				Specification.
					where(EventQuestionSpecifications.qidEquals(scoringForm.getQid())).
					and(EventQuestionSpecifications.eidEquals(scoringForm.getEid()))
				).orElse(null);
		
		if( eqEntity != null ) {
			
		}
		
		
		
		return result;
	}
	
	private AnsTblEntity getFrom(ScoringForm scoringForm,ScoringResultDto result,Integer eqId,Integer uid) {
		AnsTblEntity ansEntity = new AnsTblEntity();
		
		ansEntity.setCorrectFlg((result.isUnjustFlag()?1:0));
		ansEntity.setUid(uid);
		ansEntity.setEqid(eqId);
		ansEntity.setScore(result.getTotalScore());
		ansEntity.setTime(scoringForm.getTime());
		ansEntity.setAnswer(scoringForm.getAnswer());
		
		return ansEntity;
	}
}
