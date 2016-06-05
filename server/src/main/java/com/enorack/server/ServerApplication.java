/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server;

import com.enorack.server.rest.RESTFacade;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author pevans
 *
 */
public class ServerApplication extends Application<ServerConfiguration> {

	public static void main(String[] args) throws Exception{
		new ServerApplication().run(args);
	}
	@Override
	public String getName(){
		return "Enorack Server";
	}
	
	@Override 
	public void initialize(Bootstrap<ServerConfiguration> config){
		
	}
	@Override
	public void run(ServerConfiguration configuration, Environment environment) throws Exception {
		final RESTFacade rest = new RESTFacade();
		environment.jersey().register(rest);
	}

}
