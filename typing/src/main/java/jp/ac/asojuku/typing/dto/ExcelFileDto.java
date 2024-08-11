package jp.ac.asojuku.typing.dto;

import lombok.Data;

@Data
public class ExcelFileDto {
	private String fileName;
	private byte[] data;
}
