package jp.ac.asojuku.typing.dto.summary;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RankingSummary {
	private Integer score;
	private Integer uid;
	private Integer eid;
	
	public RankingSummary(Integer score,Integer uid,Integer eid) {
		this.score = score;
		this.uid = uid;
		this.eid = eid;
	}
	
	public RankingSummary(Object[] objects) {
	    this((Integer) ((BigDecimal)objects[0]).intValue(), (Integer) objects[1], (Integer) objects[2]);
	 }
}
