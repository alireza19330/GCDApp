package com.developairs.soap;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
//import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.developairs.dto.GCDListDTO;
import com.developairs.dto.GCDSumDTO;
import com.developairs.dto.GCD_DTO;
import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;
import com.developairs.service.MessageHandler;
import com.developairs.util.ExceptionHandler;

/**
 * 
 * @author Ali Abazari
 * <p>This class defines the web methods for SOAP service</p>
 *
 */
@WebService(serviceName="gcd")
public class GCDWebService {

	@Inject
	private MessageHandler messageHandler;

	@Inject
	private Logger logger;

	@Resource
	private WebServiceContext wsContext;
	
	private String getLogin(){
		try {
			Principal userPrincipal = wsContext.getUserPrincipal();
			return userPrincipal.getName();
		} catch (Exception e) {
			throw new GCDAppException(ResponseCode.ERR_AUTHENTICATION, "Unable to get user principal from the context", e);
		}
	}

	/**
	 * 
	 * @return {@link com.developairs.dto.GCDListDTO GCDListDTO} which contains list of GCDs and the response message
	 */
	@WebMethod
	public GCDListDTO gcdList(){

		List<Integer> gcdList = null;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		String username = "";
		try {
			username = getLogin();
			gcdList = messageHandler.getGcdList();
			logger.info("service[soap] method[gcdlist] user["+username+"] response["+ResponseCode.SUCCESSFUL+"]");
		} catch(Exception e){
			GCDAppException ex = ExceptionHandler.normalizeException(e);
			responseCode = ex.getCode().toString();
			logger.severe("service[soap] method[gcdlist] user["+username+"] response["+ex.getCode().getValue()+"]");
		}
		return new GCDListDTO(responseCode, gcdList);
	}

	/**
	 * 
	 * @return {@link com.developairs.dto.GCDSumDTO GCDSumDTO} which contains sum of all calculated GCDs and the response message
	 */
	@WebMethod
	public GCDSumDTO gcdSum(){
		long gcdSum = -1;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		String username = "";
		try {
			username = getLogin();
			gcdSum = messageHandler.getGcdSum();
			logger.info("service[soap] method[gcdsum] user["+username+"] response["+ResponseCode.SUCCESSFUL+"]");
		} catch (Exception e) {
			GCDAppException ex = ExceptionHandler.normalizeException(e);
			responseCode = ex.getCode().toString();
			logger.severe("service[soap] method[gcdsum] user["+username+"] response["+ex.getCode().getValue()+"]");
		}
		return new GCDSumDTO(responseCode, gcdSum);
	}

	/**
	 * 
	 * @return {@link com.developairs.dto.GCD_DTO GCD_DTO} which contains the GCD of the two first integer in the queue and the response message
	 */
	@WebMethod
	public GCD_DTO gcd(){
		int gcd = -1;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		String username = "";
		try {
			username = getLogin();
			gcd = messageHandler.getGCD();
			logger.info("service[soap] method[gcd] user["+username+"] response["+ResponseCode.SUCCESSFUL+"]");
		} catch (Exception e) {
			GCDAppException ex = ExceptionHandler.normalizeException(e);
			responseCode = ex.getCode().toString();
			logger.severe("service[soap] method[gcd] user["+username+"] response["+ex.getCode().getValue()+"]");
		}
		return new GCD_DTO(responseCode, gcd);
	}
}
