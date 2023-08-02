package jp.ac.asojuku.typing.form;

import lombok.Data;

@Data
public class TypingStartForm {
	private String token;
	private Integer startYear;
	private Integer startMonth;
	private Integer startDay;
	private Integer startHour;
	private Integer startMinutes;
	private Integer startSecond;

}
