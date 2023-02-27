package jp.ac.asojuku.typing.service.scoring;

import lombok.Data;

@Data
public class TypingAnswerSheet implements AnswerSheet {
	private String modelAns;	//模範解答
	private String inputAns;	//入力された解答
	
	public TypingAnswerSheet(String modelAns,String inputAns) {
		this.modelAns = modelAns;
		this.inputAns = inputAns;
	}
}	
