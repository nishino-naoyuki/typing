package jp.ac.asojuku.typing.exception;

public class EventFinishedException extends Exception {

	public EventFinishedException(String e) {
		super(e);
	}
	public EventFinishedException(Exception e) {
		super(e);
	}
}
