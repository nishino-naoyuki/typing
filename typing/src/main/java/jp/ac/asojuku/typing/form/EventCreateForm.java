package jp.ac.asojuku.typing.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class EventCreateForm {

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
}
