package jp.ac.asojuku.typing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jp.ac.asojuku.typing.exception.DonwloadfileNotFoundException;
import jp.ac.asojuku.typing.exception.EventFinishedException;
import jp.ac.asojuku.typing.exception.PermitionException;
import jp.ac.asojuku.typing.exception.SystemErrorException;

@ControllerAdvice
public class TypingExceptionAdvice {
	private static final Logger logger = LoggerFactory.getLogger(TypingExceptionAdvice.class);
	
	@ExceptionHandler(SystemErrorException.class)
	public String systemerrorExceptionHandle(SystemErrorException e) {
		logger.error("SystemErrorException",e.getMessage());
		return "/error/systemerror.html";
	}
	
	@ExceptionHandler(PermitionException.class)
	public String permitionExceptionHandle(PermitionException e) {
		logger.error("SystemErrorException",e.getMessage());
		return "/error/404.html";
	}
	
	@ExceptionHandler(EventFinishedException.class)
	public String eventFinishedExceptionHandle(EventFinishedException e) {
		logger.error("EventFinishedException",e.getMessage());
		return "/error/alreadfinished.html";
	}
	@ExceptionHandler(DonwloadfileNotFoundException.class)
	public String donwloadfileNotFoundException(DonwloadfileNotFoundException e) {
		logger.error("DonwloadfileNotFoundException",e.getMessage());
		return "/error/dlfnotfound.html";
	}
}
