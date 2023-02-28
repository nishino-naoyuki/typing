package jp.ac.asojuku.typing.param.json;

import lombok.Data;

@Data
public class OneScoringJson {
	private String result;
	private Integer qid;
	private Integer eid;
	private Integer totalScore;
	private Integer accuracyScore;	//正確性
	private Double sppedScore;		//スピード
	private boolean unjustFlag;
}
