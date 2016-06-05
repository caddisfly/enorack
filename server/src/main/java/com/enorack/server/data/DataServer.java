/**
 * Copyright 2016, Paul Evans
 */
package com.enorack.server.data;

import com.enorack.server.ServerConfiguration;

/**
 * @author pevans
 *
 */
public interface DataServer {

	public void initalize(ServerConfiguration config) throws Exception;
}
