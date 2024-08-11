package jp.ac.asojuku.typing.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EventExcelUploadForm {
	private Integer eId;
	private Integer no;
	private MultipartFile uploadFile;
}
