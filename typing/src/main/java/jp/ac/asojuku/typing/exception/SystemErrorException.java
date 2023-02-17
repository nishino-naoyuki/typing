package jp.ac.asojuku.typing.exception;

public class SystemErrorException extends Exception{
	public SystemErrorException(String e) {
		super(e);
	}
	public SystemErrorException(Exception e) {
		super(e);
	}
}
