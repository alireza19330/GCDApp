package com.developairs.rest;

import java.security.Principal;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.developairs.dto.ListDTO;
import com.developairs.dto.PushDTO;
import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;
import com.developairs.service.MessageHandler;
import com.developairs.util.ExceptionHandler;

/**
 * 
 * @author Ali Abazari
 * <p>In this class REST methods are defined</p>
 *
 */
@Path("/queue")
@RequestScoped
public class QueueRESTService {
	
	@Inject
	private Logger log;

	@Inject
	private MessageHandler messageHandler;

	@Context
	private HttpServletRequest httpServletRequest;
	
	private String getLogin(){
		try {
			Principal userPrincipal = httpServletRequest.getUserPrincipal();
			return userPrincipal.getName();
		} catch (Exception e) {
			throw new GCDAppException(ResponseCode.ERR_AUTHENTICATION, "Unable to get user principal from the context", e);
		}
	}

	/**
	 * 
	 * @return {@link com.developairs.dto.ListDTO ListDTO} object in JSON format to the clients
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ListDTO list() {
		String username = "";
		try{
			username = getLogin();
			ListDTO listDTO = new ListDTO(messageHandler.getAllMessages(), ResponseCode.SUCCESSFUL.toString());
			log.info("service[rest] method[list] user["+username+"] response["+ResponseCode.SUCCESSFUL+"]");
			return listDTO;
		} catch(Exception e){
			GCDAppException ex = ExceptionHandler.normalizeException(e);
			log.severe("service[rest] method[list] user["+username+"] response["+ex.getCode().getValue()+"]");
			return new ListDTO(null, ex.getCode().toString());
		}
	}

	/**
	 * 
	 * @param dto {@link com.developairs.dto.PushDTO pushDTO} object in JSON format to the clients
	 * @return Result of the service as a String: {@link com.developairs.exception.ResponseCode ResponseCode}
	 */
	@POST
	@Path("/push")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public String pushToQueue(PushDTO dto) {
		String username = "";
		try {
			username = getLogin();
			messageHandler.handleMessages(dto.getI1(), dto.getI2());
			log.info("service[rest] method[push] user["+username+"] response["+ResponseCode.SUCCESSFUL+"]");
			return ResponseCode.SUCCESSFUL.toString();
		} catch (Exception e) {
			GCDAppException ex = ExceptionHandler.normalizeException(e);
			log.severe("service[rest] method[push] user["+username+"] response["+ex.getCode().getValue()+"]");
			return ex.getCode().toString();
		}
	}
}
