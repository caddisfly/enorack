/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server.data.cassandra;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;
import com.enorack.server.ServerConfiguration;
import com.enorack.server.data.DataServer;

/**
 * @author pevans
 *
 */
public class CassandraDataServer implements DataServer {

	private static final Logger log = LoggerFactory.getLogger(CassandraDataServer.class);
	
	private Cluster cassandraCluster;
	private Session session;
	
	private boolean initialized = false;
	
	/**
	 * Initializes the connection to the Cassandra cluster and applies any DDLs
	 * @param config The server configuration
	 * @throws Exception if there was a problem
	 */
	@Override
	public void initalize(ServerConfiguration config) throws Exception {
		if (initialized){
			log.warn("CassandraDataServer is already initialized");
			throw new IllegalStateException("CassandraDataServer cannot be initialized again");
		}
		Collection<String> seeds = config.getDatabaseHosts();
		if (seeds.size() == 0){
			log.warn("Cassandra seed list is empty, please validate server configuration");
			throw new IllegalStateException("Cassandra seed list is empty, pleave validate server configuration");
		}
		try{
			Builder builder = Cluster.builder();
			seeds.forEach(s -> builder.addContactPoint(s));
			cassandraCluster = builder.build();
		
			log.info("Connected to Cassandra cluster: {}",cassandraCluster.getMetadata().getClusterName());
			cassandraCluster.getMetadata().getAllHosts().forEach(
					h -> log.debug("Datacenter: {} Host: {}  Rack {}",h.getDatacenter(),h.getAddress(),h.getRack()));
		
			session = cassandraCluster.connect();
			createColumnFamily(config);
			//now that we have created the keyspace (if needed) create a new session attached to that keyspace
			session.close();
			session = cassandraCluster.connect("enorack");
			createTables();
		}
		catch (Exception e){
			log.warn("Unexpected Exception initializing Cassandra Session",e);
			throw e;
		}
		initialized = true;
	}
	
	/*
	 * Creates the column family if it didnt exist already
	 */
	private void createColumnFamily(ServerConfiguration config){
		String cfCreate = "CREATE KEYSPACE IF NOT EXISTS enorack WITH replication = {'class':'SimpleStrategy', 'replication_factor':%REPLICATION_FACTOR%};";
		//replace the token %REPLICATION_FACTOR% with the actual configured value;
		cfCreate = cfCreate.replaceFirst("%REPLICATION_FACTOR%", Integer.toString(config.getReplicationFactor()));
		log.info("Creating ColumnFamily: {}",cfCreate);
		session.execute(cfCreate);	
	}
	
	/*
	 * Creates the tables if they dont exist already.  Loads the ddls from:  com/enorack/server/data/cassandra/cassandra.ddl
	 */
	private void createTables() throws IOException{
		List<String> statements = loadDDLResource("com/enorack/server/data/cassandra/cassandra.ddl");
		statements.forEach(s ->{
			log.info("Executing DDL: {}",s);
			session.execute(s);
		});
	}
	
	/**
	 * Loads the DDL statements from a resource, as a convinience, it appends a ; if there wasnt one. Lines starting
	 * '#' are comments and are ignored.
	 * @param resourcePath The path to the resource
	 * @return a list of DDL statements
	 * @throws IOException If the resource could not be loaded
	 */
	List<String> loadDDLResource(String resourcePath) throws IOException{
		InputStream in = null;
		try{
			in = getClass().getClassLoader().getResourceAsStream(resourcePath);
			if (null == in){
				log.warn("Unable to load resource {}",resourcePath);
				throw new FileNotFoundException("Unable to load resource " + resourcePath);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			List<String> ret = new LinkedList<>();
			reader.lines().forEach(l -> {
				final String trimmed = l.trim();
				if (trimmed.startsWith("#")){
					//skip comments
					return;
				}
				if (! trimmed.trim().endsWith(";")){
					ret.add(trimmed+";");
				}
				else{
					ret.add(trimmed);
				}
			});
			return ret;
		}
		finally{
			if (null != in){
				in.close();
			}
		}
		
	}

}
