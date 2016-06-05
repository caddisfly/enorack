/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.enorack.server.data.cassandra.CassandraDataServer;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

/**
 * @author pevans
 *
 */
public class ServerConfiguration extends Configuration {

	Collection<String> seedIps = Arrays.asList(new String[]{"127.0.0.1"});
	String dataServerClass = CassandraDataServer.class.getCanonicalName();
	
	@JsonProperty
	public void setDatabaseHosts(Collection<String> seedIps){
		this.seedIps = seedIps;
	}
	
	public Collection<String> getDatabaseHosts(){
		return Collections.unmodifiableCollection(seedIps);
	}
	
	@JsonProperty
	public void setDataServerClass(String className){
		dataServerClass = className;
	}
	
	public String getDataServerClass(){
		return dataServerClass;
	}
}
