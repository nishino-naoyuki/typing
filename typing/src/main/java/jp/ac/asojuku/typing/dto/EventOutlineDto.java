package jp.ac.asojuku.typing.dto;

import java.util.Date;

import jp.ac.asojuku.typing.param.EventState;
import jp.ac.asojuku.typing.util.Exchange;
import lombok.Data;

@Data
public class EventOutlineDto {
	private Integer eid;
	private String name;
	private Date publicDateTime;
	private Date startDateTime;
	private Date endDateTime;
	private EventState eventState;
	private String ownerName;
	private boolean joinFlag;
	private String token;
	
	public String getPublicDateString() {
		return Exchange.toFormatString(publicDateTime, "yyyy/MM/dd HH:mm:ss");
	}
	public String getStartDateString() {
		return Exchange.toFormatString(startDateTime, "yyyy/MM/dd HH:mm:ss");
	}
	public String getEndDateString() {
		return Exchange.toFormatString(endDateTime, "yyyy/MM/dd HH:mm:ss");
	}
	public String getStateString() {
		return eventState.getMsg();
	}
	public Integer getStateId() {
		return eventState.getId();
	}
	
	/**
	 * 登録ボタンを表示するかどうかの判断
	 * あまりDTOにロジックは持ちたくないが、、、VIEW側でゴリゴリ書くのもね
	 * 
	 * @return
	 */
	public boolean isDidaplyEntryButton() {
		Date now = new Date();
		return ( now.after(publicDateTime) && now.before(startDateTime) && !joinFlag );
	}
}
