package jp.ac.asojuku.typing.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.typing.util.Exchange;
import lombok.Data;

@Data
public class EventInfoDetailDto {

	private Integer eid;
	private String eventname;
	private Date publicstartdatetime;
	private Date startdatetime;
	private Date enddatetime;
	private Date publicenddatetime;
	private String ownername;
	private Boolean rankingdisplay;
	private Integer hiderankingtime;
	private String filter;
	private List<DownloadQFileDto> downloadqFileList;
	
	private List<QuestionOutlineDto> qList;
	private List<DwonloadQOutlineDto> dqList;
	
	public String getPublicStartDateString() {
		return Exchange.toFormatString(publicstartdatetime,"yyyy-MM-dd'T'HH:mm:ss");
	}
	public String getPStartDateString() {
		return Exchange.toFormatString(startdatetime,"yyyy-MM-dd'T'HH:mm:ss");
	}
	public DownloadQFileDto getDownloadQFileDto(int index) {
		if( downloadqFileList == null || downloadqFileList.size() <= index) {
			return null;
		}
		return downloadqFileList.get(index);
	}
}
