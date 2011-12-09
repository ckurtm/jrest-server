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

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.peirr.jrestserver.reactors.ResponseReactor;

/**
 * 
 * @author kurt.mbanje ckurtm at gmail dot com
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
