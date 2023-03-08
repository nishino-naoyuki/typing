package jp.ac.asojuku.typing.param.csv;

public class EventResultCSV {
	private StringBuilder lineData;

	public void addData(String info) {
		if( lineData == null ) {
			lineData = new StringBuilder("\""+info+"\"");
		}else {
			lineData.append(",").append("\""+info+"\"");
		}
	}
	
	public String toString() {
		if( lineData == null ) {
			return "";
		}
		return lineData.toString();
	}
	
}
