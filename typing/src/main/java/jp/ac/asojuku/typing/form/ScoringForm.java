package jp.ac.asojuku.typing.form;

import lombok.Data;

@Data
public class ScoringForm {
	private String answer;
	private Integer submitYear;
	private Integer submitMounth;
	private Integer submitday;
	private Integer submitHour;
	private Integer submitMinutes;
	private Integer submitSecond;
	private Integer keycount;
	private Integer time;
}
