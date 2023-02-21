package jp.ac.asojuku.typing.dto;

import lombok.Data;

/**
 * ユーザー登録のDTO
 * @author nishino
 *
 */
@Data
public class CreateUserDto {
	private Integer roleId;	
	private String mail;
	private String name;
	private String dispName;
	private String password;
	private String affiliation;
}
