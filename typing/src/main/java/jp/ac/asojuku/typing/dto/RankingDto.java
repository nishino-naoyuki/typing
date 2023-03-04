package jp.ac.asojuku.typing.dto;

import lombok.Data;

@Data
public class RankingDto {
	private Integer uid;
	private Integer ranking;
	private String dispName;
	private String name;
	private String mail;
	private Integer score;
	private String affiliation;
}
