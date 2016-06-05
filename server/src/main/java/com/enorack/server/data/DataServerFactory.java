/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enorack.server.ServerConfiguration;

/**
 * @author pevans
 *
 */
public abstract class DataServerFactory {
	
	private static final Logger log = LoggerFactory.getLogger(DataServerFactory.class);
	private static boolean initialized = false;
	static DataServer server;
	
	/**
	 * Creates and initializes a DataServer implementation based on the server configuration
	 * @param configuration The server configuration
	 * @throws Exception If there was a problem with the configuration or connecting to the remote database
	 */
	public static void initialize(ServerConfiguration configuration) throws Exception{
		log.info("Attempting to load DataServer implementation: {0}",configuration.getDataServerClass());
		server = (DataServer) Class.forName(configuration.getDataServerClass()).newInstance();
		server.initalize(configuration);
		initialized = true;
		log.info("initialized");
	}
	
	/**
	 * 
	 * @return An instance of DataServer that can be used to access the data store
	 */
	public static DataServer getDataServer(){
		if (! initialized ){
			log.warn("DataServerFactory.getInstance() called before initialization");
			throw new IllegalStateException("DataServerFactory must first be initialized");
		}
		return server;
	}
}
