package jp.ac.asojuku.typing.form;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class UserCreateForm {
	@Email
	private String mail;
	private String name;
	private String dispName;
	private String password;
	private String affiliation;
	private String token;
}
