package jp.ac.asojuku.typing.dto;

import java.util.List;

import lombok.Data;

@Data
public class QuestionDetailDto {
	private Integer qid;
	private String title;
	private String sentence;
	private Integer difficulty;
	private String answer;
	private List<EventOutlineDto> eventList;
}
