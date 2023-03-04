package jp.ac.asojuku.typing.dto.summary;

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
	    this((Integer) objects[0], (Integer) objects[1]);
	 }
}
