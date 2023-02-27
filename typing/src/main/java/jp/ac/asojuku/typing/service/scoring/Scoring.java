package jp.ac.asojuku.typing.service.scoring;

import jp.ac.asojuku.typing.dto.ScoringResultDto;

public interface Scoring {
	public ScoringResultDto doScoring(AnswerSheet ansSheet);
}
