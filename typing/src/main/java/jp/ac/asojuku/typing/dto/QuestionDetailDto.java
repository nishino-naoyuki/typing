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
	
	//2023.8.1追加 開始ボタンが押されたときの処理
	private boolean isStarted;
	private String token;
	private Integer startYear;
	private Integer startMonth;
	private Integer startDay;
	private Integer startHour;
	private Integer startMinutes;
	private Integer startSecond;
	
	public Integer getStartMonth0Orgin() {
		return (startMonth == null ? null : startMonth-1);
	}
}
