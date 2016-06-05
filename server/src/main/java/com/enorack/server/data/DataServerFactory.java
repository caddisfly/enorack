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
	
	public static void initialize(ServerConfiguration configuration){
		initialized = true;
		log.info("DataServerFactory initialized");
	}
	
	public static DataServer getInstance(){
		if (! initialized ){
			log.warn("DataServerFactory.getInstance() called before initialization");
			throw new IllegalStateException("DataServerFactory must first be initialized");
		}
		// TODO: return something actually useful
		return null;
	}
}
