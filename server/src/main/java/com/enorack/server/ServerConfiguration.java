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

	private Collection<String> seedIps = Arrays.asList(new String[]{"127.0.0.1"});
	private String dataServerClass = CassandraDataServer.class.getCanonicalName();
	private int replicationFactor = 1;
	
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
	
	@JsonProperty
	public void setReplicationFactor(int factor){
		this.replicationFactor = factor;
	}
	
	public int getReplicationFactor(){
		return replicationFactor;
	}
}
