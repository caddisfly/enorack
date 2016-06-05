/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author pevans
 *
 */
@Path("/1.0/enorack")
public class RESTFacade {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/cellars")
	public Response getCellars(){
		return Response.ok("{[{\"id\":1}]}").build();
	}
}
