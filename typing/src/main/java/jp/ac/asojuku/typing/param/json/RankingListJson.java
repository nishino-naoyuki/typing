package jp.ac.asojuku.typing.param.json;

import java.util.ArrayList;
import java.util.List;

import jp.ac.asojuku.typing.dto.RankingDto;
import lombok.Data;

@Data
public class RankingListJson {
	private String getTime;
	private Boolean isDisplay;
	private List<RankingDto> rankingList = new ArrayList<>();
}
