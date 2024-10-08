package jp.ac.asojuku.typing.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EventCreateForm {
	
	private Integer eid;

	@NotEmpty(message = "{errmsg0201}")
	@Size(max = 200, message="{errmsg0207}")
	private String eventname;

	@NotEmpty(message = "{errmsg0202}")
	private String publicstartdatetime;

	@NotEmpty(message = "{errmsg0203}")
	private String startdatetime;

	@NotEmpty(message = "{errmsg0204}")
	private String enddatetime;

	@NotEmpty(message = "{errmsg0205}")
	private String publicenddatetime;

	@NotEmpty(message = "{errmsg0206}")
	private String ownername;
	
	private Integer rankingdisplay;
	@Range(min=0, max=9999,message = "{errmsg0208}" )
	private Integer hiderankingtime;
	private String filter;
	private Integer[] qidList;
	private MultipartFile downloadQFile1;
	private MultipartFile downloadQFile2;
	private MultipartFile downloadQFile3;
	private MultipartFile downloadQFile4;
	private MultipartFile downloadQFile5;
	private Integer downloadQFile1Id;
	private Integer downloadQFile2Id;
	private Integer downloadQFile3Id;
	private Integer downloadQFile4Id;
	private Integer downloadQFile5Id;
}
