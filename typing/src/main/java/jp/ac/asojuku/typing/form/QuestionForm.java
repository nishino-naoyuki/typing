package jp.ac.asojuku.typing.form;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class QuestionForm {
	
	private Integer qid;	//更新の時に入る
	
	private Boolean practiceFlg;
	
	@NotEmpty(message = "{errmsg0101}")
	@Size(max = 100, message="{errmsg0103}")
	private String title;

	@NotEmpty(message = "{errmsg0102}")
	@Size(max = 5000, message="{errmsg0104}")
	private String question;
	
	private Integer difficulty;

}
