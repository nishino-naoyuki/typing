package jp.ac.asojuku.typing.dto;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DownloadQFileDto {
	private Integer uploadfileId;
	private File uploadfile;
	
	public DownloadQFileDto(Integer uploadfileId,File uploadfile) {
		this.uploadfileId = uploadfileId;
		this.uploadfile = uploadfile;
	}
}
