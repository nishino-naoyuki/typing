package jp.ac.asojuku.typing.param.json;

import lombok.Data;

@Data
public class ErrorField {
	private String field;
	private String msg;
}
