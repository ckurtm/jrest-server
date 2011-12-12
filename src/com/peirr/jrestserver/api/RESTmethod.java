/***
 * Copyright (C) 2011  kurt.mbanje peirr solutions
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * @author kurt.mbanje ckurtm at gmail.com
 *
 */

package com.peirr.jrestserver.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.peirr.jrestserver.Core;
import com.peirr.jrestserver.reactors.ResponseFormat;
import com.peirr.jrestserver.reactors.ResponseReactor;

/**
 * 
 * @author kurt.mbanje ckurtm at gmail dot com
 * This has all method implementations for {@link RESTinterface}
 */
public class RESTmethod implements RESTinterface {
	private Connection      connection;
	private ResultSet       resultset;
	private String          connectionURL = Core.CONN_URL;
	private boolean         connectionOpen;

	private ResponseReactor reactor;


	public RESTmethod() throws SQLException {
		DriverManager.registerDriver(new AppEngineDriver());
		reactor = new ResponseReactor();
	}

	public Connection open(){
		if(!connectionOpen){
			try {
				connection = DriverManager.getConnection(connectionURL);
				connectionOpen = true;
			} catch (SQLException e) {
				e.printStackTrace();
				connectionOpen = false;
			}
		}
		return connection;
	}

	public void close(){
		if(connection != null){
			try {
				connection.close();

			} catch (SQLException ignore) {
				ignore.printStackTrace();
			}
			connectionOpen = false;
		}
	}
	//************** METHODS

    /**
     * gets the required output format from the request. Defaults to JSON if no format parameter can be found.
     * @param args
     * @return
     */
	private ResponseFormat getFormat(HashMap<String, String> args){
		ResponseFormat fmt = ResponseFormat.JSON;
		if(args.containsKey("format")){
			String f = args.get("format");
			if(f.toLowerCase().equals("xml")){
				fmt = ResponseFormat.XML;
			}else if(f.toLowerCase().equals("json")){
				fmt = ResponseFormat.JSON;
			}else{ //default formating to json
				fmt = ResponseFormat.JSON;
			}		        	
		}
		return fmt;
	}
	
	/**
	 * Example method 1<br/>
	 * simple INFO response method
	 */
	@Override
	public String say(HashMap<String, String> args) {	
		return reactor.create(null,"say hello : " + args.get("name") + " surname: " + args.get("surname"),getFormat(args));
	}

    
	/**
	 * Example method 2<br/>
	 * simple DATA response method that get data from a database
	 */
	@Override
	public String version(HashMap<String, String> args) {
		String data = reactor.createEmptyJSONResponse();
		System.out.println("version() [args:"+args+"][format:"+getFormat(args)+"]");
		try {
			//TODO figure out better method for connection pooling
			open();
			resultset = connection.createStatement().executeQuery("SELECT * FROM versions");
			data = reactor.create(resultset,null,getFormat(args));
		} catch (SQLException e) {
			data = reactor.create(null,e.getMessage(),getFormat(args));		
		}finally{
			close();
		}	
		return data;
	}



}
