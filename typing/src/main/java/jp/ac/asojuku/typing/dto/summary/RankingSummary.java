package jp.ac.asojuku.typing.dto.summary;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RankingSummary {
	private Integer score;
	private Integer uid;
	
	public RankingSummary(Integer score,Integer uid) {
		this.score = score;
		this.uid = uid;
	}
	
	public RankingSummary(Object[] objects) {
	    this((Integer) ((BigDecimal)objects[0]).intValue(), (Integer) objects[1]);
	 }
}
