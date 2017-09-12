package com.developairs.soap;

import java.util.List;

import javax.inject.Inject;
//import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.developairs.service.MessageHandler;

@WebService(serviceName="gcd")
public class GCDWebService {
	
	@Inject
	private MessageHandler messageHandler;
	
	@WebMethod
	public List<Integer> gcdList(){
		List<Integer> gcdList = null;
		try {
			gcdList = messageHandler.gcdList();
		} catch (Exception e) {
			// TODO: log
		}
		return gcdList;
	}

	@WebMethod
	public long gcdSum(){
		long gcdSum = -1;
		try {
			gcdSum = messageHandler.gcdSum();
		} catch (Exception e) {
			// TODO: log
		}
		return gcdSum;
	}
	
	@WebMethod
	public int gcd(){
		try {
			return messageHandler.getGCD();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error in handling service:"+e.getMessage());
			return -1;
		}
	}
}
