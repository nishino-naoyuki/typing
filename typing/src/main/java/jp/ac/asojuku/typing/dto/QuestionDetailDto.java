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
	private String answer;
	private List<EventOutlineDto> eventList;
	
	public String getSentenceBR() {
		return HtmlUtil.nl2be(sentence);
	}
}
