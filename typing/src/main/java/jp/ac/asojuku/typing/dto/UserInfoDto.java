package jp.ac.asojuku.typing.dto;

import lombok.Data;

/**
 * ユーザー更新のDTO
 * @author nishino
 *
 */
@Data
public class UserInfoDto {
	private Integer uid;
	private Integer roleId;	
	private String mail;
	private String name;
	private String dispName;
	private String affiliation;

}
