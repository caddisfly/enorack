/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server.data.cassandra;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


/**
 * @author pevans
 *
 */
public class CassandraDataServerTest {

	@Test
	public void testLoadDDLResource() {
		CassandraDataServer cds = new CassandraDataServer();
		try{
			List<String> lines = cds.loadDDLResource("com/enorack/server/data/cassandra/unittest.ddl");
			assertNotNull(lines);
			assertTrue(lines.size() == 3);
			lines.forEach(l -> assertTrue(l.endsWith(";")));
		}
		catch (Exception e){
			e.printStackTrace(System.out);
			fail("shit broke");
		}
	}

}
