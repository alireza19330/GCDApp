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

import com.developairs.rest.dto.ListDTO;
import com.developairs.rest.dto.PushDTO;
import com.developairs.service.MessageHandler;
import com.developairs.util.Constants;


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
        return new ListDTO(messageHandler.getAllMessages());
    }

    @POST
    @Path("/push")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pushToQueue(PushDTO dto) {
    	try{
    		messageHandler.register(dto.getI1(), dto.getI2());
    	}catch(Exception e){
    		log.severe("unable to register parameters<"+dto.getI1()+","+dto.getI2()+"> "+e.getMessage());
    		return Constants.MSG_FAIL;
    	}
        return Constants.MSG_SUCCESSFUL;
    }
}
