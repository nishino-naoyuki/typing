package jp.ac.asojuku.typing.dto;

import lombok.Data;

@Data
public class DwonloadQOutlineDto {

	private Integer downloadfileId;
	private String downloadfilePath;
	private String downloadfileName;
	private Integer no;
	private boolean uploaded;	//アップロード済みかのフラグ
	private boolean dlOk;		//ダウンロード可能か
}
