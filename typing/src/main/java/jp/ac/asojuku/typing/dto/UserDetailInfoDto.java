package jp.ac.asojuku.typing.dto;

import java.util.List;

import jp.ac.asojuku.typing.param.RoleId;
import lombok.Data;

@Data
public class UserDetailInfoDto {
	private String token;
	private Integer uid;
	private Integer roleId;	
	private String mail;
	private String name;
	private String dispName;
	private String affiliation;
	private List<PersonalEventInfoDto> personalEventInfoList;
	private Boolean editable;
	
	public String getRoleName() {
		return RoleId.toString(roleId);
	}
}
