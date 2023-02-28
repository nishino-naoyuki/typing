package jp.ac.asojuku.typing.dto;

import lombok.Data;

@Data
public class ScoringResultDto {
	private Integer totalScore;
	private Integer accuracyScore;	//正確性
	private Double sppedScore;		//スピード
	private boolean unjustFlag;
	
}
