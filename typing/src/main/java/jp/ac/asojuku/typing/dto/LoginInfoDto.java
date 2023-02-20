package jp.ac.asojuku.typing.dto;

import jp.ac.asojuku.typing.param.RoleId;
import lombok.Data;

@Data
public class LoginInfoDto {
	private Integer uid;
	private String name;
	private String mail;
	private RoleId role;
}
