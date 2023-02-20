package jp.ac.asojuku.typing.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UserInputCSVForm {
	private MultipartFile inputuserfile;
}
