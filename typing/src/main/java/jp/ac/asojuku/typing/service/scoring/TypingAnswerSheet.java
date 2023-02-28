package jp.ac.asojuku.typing.service.scoring;

import lombok.Data;

@Data
public class TypingAnswerSheet implements AnswerSheet {
	private String modelAns;	//模範解答
	private String inputAns;	//入力された解答
	private Integer time;		//入力にかかった時間
	private Integer keyupCount;	//キーアップの回数
	
	public TypingAnswerSheet(String modelAns,String inputAns,Integer time,Integer keyupCount) {
		this.modelAns = modelAns;
		this.inputAns = inputAns;
		this.time = time;
		this.keyupCount = keyupCount;
	}
}	
