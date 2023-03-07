package jp.ac.asojuku.typing.dto;

import java.util.Date;

import lombok.Data;

/**
 * 問題リスト情報
 * @author nishino
 *
 */
@Data
public class QuestionOutlineDto {
	private Integer qid;
	private String title;
	private Integer difficulty;
	private Date submitTime;
	private Integer practiceFlg;
	private String kindName;
	private String submitTimeString;
	private String score;
	private Integer no;
	private Integer eqid;
}
