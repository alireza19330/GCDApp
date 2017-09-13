package com.developairs.rest;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.developairs.dto.ListDTO;
import com.developairs.dto.PushDTO;
import com.developairs.exception.GCDAppException;
import com.developairs.exception.ResponseCode;
import com.developairs.service.MessageHandler;


@Path("/queue")
@RequestScoped
public class QueueRESTService {
    @Inject
    private Logger log;

    @Inject
    private MessageHandler messageHandler;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ListDTO list() {
    	try{
    		ListDTO listDTO = new ListDTO(messageHandler.getAllMessages(), ResponseCode.SUCCESSFUL.toString());
    		log.info("service[rest] method[list] user[] response["+ResponseCode.SUCCESSFUL+"]");
    		return listDTO;
    	} catch(GCDAppException e){
    		log.severe("service[rest] method[list] user[] response["+e.getCode().getValue()+"]");
    		return new ListDTO(null, e.getCode().toString());
    	} catch (Exception e) {
			log.severe("service[rest] method[list] user[] response["+ResponseCode.ERR_UNKNOW+"]");
			return new ListDTO(null, ResponseCode.ERR_UNKNOW.toString());
		}
    }

	@POST
    @Path("/push")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pushToQueue(PushDTO dto) {
    	try {
    		messageHandler.handleMessages(dto.getI1(), dto.getI2());
    		log.info("service[rest] method[push] user[] response["+ResponseCode.SUCCESSFUL+"]");
    		return ResponseCode.SUCCESSFUL.toString();
		} catch (GCDAppException e) {
			log.severe("service[rest] method[push] user[] response["+e.getCode().getValue()+"]");
			return e.getCode().toString();
		} catch (Exception e) {
			log.severe("service[rest] method[push] user[] response["+ResponseCode.ERR_UNKNOW+"]");
			return ResponseCode.ERR_UNKNOW.toString();
		}
    }
}
