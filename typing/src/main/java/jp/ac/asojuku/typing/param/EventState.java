package jp.ac.asojuku.typing.param;

public enum EventState {

	PREPUBLIC(0,"公開前"),
	PRE(1,"開催前"),
	INPROGRESS(2,"開催中"),
	AFTER(3,"終了");

	//ステータス
	private int id;
	private String msg;

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return msg1
	 */
	public String getMsg() {
		return msg;
	}


	private EventState(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	public boolean equals(Integer id){
		if(id == null){
			return false;
		}

		return (this.id == id);
	}

	public static EventState search(Integer id){
		EventState eventState = null;
		if( PREPUBLIC.equals(id)){
			eventState = PREPUBLIC;
		}else if( PRE.equals(id)){
			eventState = PRE;
		}else if( INPROGRESS.equals(id)){
			eventState = INPROGRESS;
		}else if( AFTER.equals(id)){
			eventState = AFTER;
		}

		return eventState;
	}

	public static boolean check(int id){
		boolean ret = false;

		if( PRE.equals(id)){
			ret = true;
		}else if( INPROGRESS.equals(id)){
			ret = true;
		}

		return ret;
	}
}
