package jp.ac.asojuku.typing.param.json;

import java.util.List;

import jp.ac.asojuku.typing.dto.DwonloadQOutlineDto;
import lombok.Data;

@Data
public class ExcelQuestionJson {
	private String result;
	List<DwonloadQOutlineDto> dlqList;
}
