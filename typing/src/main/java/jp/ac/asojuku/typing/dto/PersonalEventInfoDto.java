package jp.ac.asojuku.typing.dto;

import lombok.Data;

@Data
public class PersonalEventInfoDto {
	private Integer submitCount = 0;
	private Integer totalScore = 0;
	private Integer rank = 0;
	private String getTime;
}
