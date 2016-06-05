/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enorack.server.data.DataServerFactory;
import com.enorack.server.rest.RESTFacade;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author pevans
 *
 */
public class ServerApplication extends Application<ServerConfiguration> {

	private static Logger log = LoggerFactory.getLogger(ServerApplication.class);
	public static void main(String[] args) throws Exception{
		new ServerApplication().run(args);
	}
	@Override
	public String getName(){
		return "Enorack Server";
	}
	
	@Override 
	public void initialize(Bootstrap<ServerConfiguration> bootstrap){

	}
	@Override
	public void run(ServerConfiguration configuration, Environment environment) throws Exception {
		try{
			log.info("Initializing DataServerFactory");
			DataServerFactory.initialize(configuration);
		}
		catch (Exception e){
			log.warn("Execption initalizing DataServerFactory",e);
			try{
				Thread.sleep(10000);
			}
			catch (InterruptedException ie){
				// to bad
			}
		}
		final RESTFacade rest = new RESTFacade();
		environment.jersey().register(rest);
	}

}
