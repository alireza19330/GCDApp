package com.developairs.exception;

import javax.ejb.ApplicationException;


/**
 * 
 * @author Ali Abazari
 * <p>This class is created for unifying exception handling, it contains the <b>response code</b> which per se presents the reason for the exception and 
 * <b>exception message</b> which is additional info to find the cause and the <b>original exception</b> that is wrapped by the instance of this class</p>
 *
 */
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
