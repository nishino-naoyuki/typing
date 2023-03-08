package jp.ac.asojuku.typing.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailInfoDto {
	private Integer uid;
	private Integer roleId;	
	private String mail;
	private String name;
	private String dispName;
	private String affiliation;
	private List<PersonalEventInfoDto> personalEventInfoList;
	private boolean isEditable;
}
