package com.peirr.jrestserver.api;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.peirr.jrestserver.reactors.ResponseReactor;

/**
 * 
 * @author ckurtm at gmail dot com
 * This has all method implementations for {@link RESTinterface}
 */
public class RESTmethod implements RESTinterface {
	private ResponseReactor reactor;
	private static final char[] symbols = new char[36];
	private final Random random = new Random();
	private char[] buf;

	public RESTmethod() throws SQLException{
		DriverManager.registerDriver(new AppEngineDriver());
		reactor = new ResponseReactor();
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}


	String randomString(int length){
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx) 
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);

	}


	//************** METHODS


	@Override
	public String say(HashMap<String, String> args) {
		return reactor.create(null,"say hello : " + args.get("name") + " surname: " + args.get("surname"));
	}


	@Override
	public String version(HashMap<String, String> args) {
		// TODO Auto-generated method stub
		return null;
	}



}
