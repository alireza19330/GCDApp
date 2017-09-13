package com.developairs.exception;

import javax.ejb.ApplicationException;



@ApplicationException(rollback=true)
public class GCDAppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ResponseCode code;
	private Exception originalException;
	
	public ResponseCode getCode() {
		return code;
	}
	public Exception getOriginalException() {
		return originalException;
	}
	
	public GCDAppException() {
		super();
	}
	
	public GCDAppException(String message){
		super(message);
	}
	
	public GCDAppException(ResponseCode code, String message, Exception originalException){
		super(message);
		this.originalException = originalException;
		this.code = code;
	}

}
