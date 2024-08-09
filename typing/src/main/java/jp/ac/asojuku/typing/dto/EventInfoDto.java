package jp.ac.asojuku.typing.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class EventInfoDto {
	private Integer eid;
	private String name;
	private Date startDateTime;
	private Date endDateTime;
	private boolean rankingDisplay;
	private boolean displayQuestion;
	private boolean pastEvent;
	private boolean beforeEvent;
	private List<QuestionOutlineDto> qList;
	private List<UserInfoDto> uList;
	private RankingDto userRanking;
	private long lefttime;	//開始までの残り時間
	private boolean entryFlag;
	private List<DwonloadQOutlineDto> dqList;
}
