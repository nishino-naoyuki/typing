package jp.ac.asojuku.typing.dto;

import java.util.Date;
import java.util.List;

import jp.ac.asojuku.typing.util.Exchange;
import lombok.Data;

@Data
public class EventInfoDetailDto {

	private Integer eid;
	private String eventname;
	private Date publicstartdatetime;
	private Date startdatetime;
	private Date enddatetime;
	private Date publicenddatetime;
	private String ownername;
	private Boolean rankingdisplay;
	private Integer hiderankingtime;
	private String filter;
	
	private List<QuestionOutlineDto> qList;
	
	public String getPublicStartDateString() {
		return Exchange.toFormatString(publicstartdatetime,"yyyy-MM-dd'T'HH:mm:ss");
	}
	public String getPStartDateString() {
		return Exchange.toFormatString(startdatetime,"yyyy-MM-dd'T'HH:mm:ss");
	}
}
