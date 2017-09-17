package com.developairs.util;

import javax.ejb.EJBAccessException;

import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;

/**
 * 
 * @author Ali Abazari
 *
 */
public class ExceptionHandler {
	
	public static GCDAppException normalizeException(Exception e){
		if (e instanceof GCDAppException) {
			return (GCDAppException)e;
		} else if (e instanceof EJBAccessException){
			return new GCDAppException(ResponseCode.ERR_AUTHORIZATION, "Unauthorized request", e);
		}
		return new GCDAppException(ResponseCode.ERR_UNKNOW, "Exception", e);
	}

}
