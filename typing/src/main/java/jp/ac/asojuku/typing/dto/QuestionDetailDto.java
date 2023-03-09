package jp.ac.asojuku.typing.dto;

import java.util.List;

import jp.ac.asojuku.typing.util.HtmlUtil;
import lombok.Data;

@Data
public class QuestionDetailDto {
	private Integer eid;
	private Integer qid;
	private String title;
	private String sentence;
	private Integer difficulty;
	private List<EventOutlineDto> eventList;
	
	//option  セットされない場合がある
	private String answer;
	private Boolean practiceFlg;
	
	public String getSentenceBR() {
		return HtmlUtil.nl2be(sentence);
	}
}
