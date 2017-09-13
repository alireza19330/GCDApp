package com.developairs.soap;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
//import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.developairs.dto.GCDListDTO;
import com.developairs.dto.GCDSumDTO;
import com.developairs.dto.GCD_DTO;
import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;
import com.developairs.service.MessageHandler;

@WebService(serviceName="gcd")
public class GCDWebService {

	@Inject
	private MessageHandler messageHandler;

	@Inject
	private Logger logger;

	@WebMethod
	public GCDListDTO gcdList(){
		List<Integer> gcdList = null;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		try {
			gcdList = messageHandler.gcdList();
			logger.info("service[soap] method[gcdlist] user[] response["+ResponseCode.SUCCESSFUL+"]");
		} catch (GCDAppException e) {
			responseCode = e.getCode().toString();
			logger.severe("service[soap] method[gcdlist] user[] response["+e.getCode().getValue()+"]");
		} catch (Exception e) {
			responseCode = ResponseCode.ERR_UNKNOW.toString();
			logger.severe("service[soap] method[gcdlist] user[] response["+ResponseCode.ERR_UNKNOW+"]");
		}
		return new GCDListDTO(responseCode, gcdList);
	}

	@WebMethod
	public GCDSumDTO gcdSum(){
		long gcdSum = -1;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		try {
			gcdSum = messageHandler.gcdSum();
			logger.info("service[soap] method[gcdsum] user[] response["+ResponseCode.SUCCESSFUL+"]");
		} catch (GCDAppException e) {
			responseCode = e.getCode().toString();
			logger.severe("service[soap] method[gcdsum] user[] response["+e.getCode().getValue()+"]");
		} catch (Exception e) {
			responseCode = ResponseCode.ERR_UNKNOW.toString();
			logger.severe("service[soap] method[gcdsum] user[] response["+ResponseCode.ERR_UNKNOW+"]");
		}
		return new GCDSumDTO(responseCode, gcdSum);
	}

	@WebMethod
	public GCD_DTO gcd(){
		int gcd = -1;
		String responseCode = ResponseCode.SUCCESSFUL.toString();
		try {
			gcd = messageHandler.getGCD();
			logger.info("service[soap] method[gcd] user[] response["+ResponseCode.SUCCESSFUL+"]");
		} catch (GCDAppException e) {
			responseCode = e.getCode().toString();
			logger.severe("service[soap] method[gcd] user[] response["+e.getCode().getValue()+"]");
		} catch (Exception e) {
			responseCode = ResponseCode.ERR_UNKNOW.toString();
			logger.severe("service[soap] method[gcd] user[] response["+ResponseCode.ERR_UNKNOW+"]");
		}
		return new GCD_DTO(responseCode, gcd);
	}
}
