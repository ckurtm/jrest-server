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

package com.peirr.jrestserver.reactors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.peirr.jrestserver.Core;
import com.peirr.jrestserver.api.RESTmethod;

/**
 * Validates a request to note if we should proceed with processing
 * @author kurt.mbanje ckurtm at gmail dot com
 *
 */
public class RequestHelper {
	String tag = RequestHelper.class.getSimpleName();

	private boolean                 valid;
	private HashMap<String, String> params = new HashMap<String, String>();
	private String 				    path;
	private String    				message;
	private Method                  method;

	private  String   PARAM_VERSION  = "vs";
	private  String   PARAM_METHOD   = "m";
	private  String   PARAM_OS       = "os";

	public RequestHelper(String s){
		path = s;
		validateRequest();
	}

	public RequestHelper(HashMap<String, String> params){
		this.params = params;
		validateRequest(params);
	}

	@Override
	public String toString() {
		return "RequestHelper [valid=" + valid + ", params=" + params + ", path=" + path + "]";
	}


	/**
	 * validates the request to make sure we can handle the supplied parameters
	 * @param params
	 */
	private void validateRequest(HashMap<String,String> params){
		System.out.println("validateRequest: " + params);
		valid   =  false;
		message = Core.UNKNOWN_ERROR;
		if((params != null) && (params.size() > 0)){
	

			//has method
			if(hasMethodParameter(params)){
				if(validMethod(params.get(PARAM_METHOD))){
					System.out.println("i got a valid method");
					message = Core.VALID_PARAMS;
					valid   =  true;
				}else{
					message = Core.INVALID_REQUEST + " [unknown method]";
					valid   =  false;
				}
			}else{
				message = Core.INVALID_REQUEST + " [method missing]";
				valid   =  false; 
			}					

		}else{
			message = Core.INVALID_PARAMS + " [uneven]";
		}

	}

	/**
	 * Validate the request using the following rules:<br/>
	 *  - should have method param (m)<br/>
	 */
	private void validateRequest(){
		params.clear();
		valid   =  false;
		message = Core.UNKNOWN_ERROR;
		String[] s  = new String[0];
		if(path != null){
			//			System.out.println("LENGTH: " + path.split("/").length);
			s = path.split("/");
		}

		ArrayList<String> strparams = new ArrayList<String>();
		for(int i=0;i<s.length;i++){
			if(!((String)(s[i])).trim().isEmpty()){
				strparams.add(s[i]);
			}else{
				//				System.out.println("skipping: " + s[i]);
			}
		}

		if((path != null) && ((strparams.size() % 2) == 0)){
			String[] sa = strparams.toArray(new String[strparams.size()]);
			if(hasEvenParameters(sa)){
				for(int i=0;i<sa.length/2;i++){
					System.out.println((sa[i*2]) + " -> " + sa[(i*2)+1]);
					params.put(sa[i*2],sa[(i*2)+1]);             	
				}



				//has method
				if(hasMethodParameter(params)){
					if(validMethod(params.get(PARAM_METHOD))){
						message = Core.VALID_PARAMS;
						valid   =  true;
					}else{
						message = Core.INVALID_REQUEST + " [unknown method]";
						valid   =  false;
					}
				}else{
					message = Core.INVALID_REQUEST + " [method missing]";
					valid   =  false; 
				}
			}else{
				message = Core.INVALID_PARAMS + " [uneven]";
			}
		}else{
			message = Core.INVALID_PARAMS;
			valid   =  false; 
		}		
	}

	boolean hasVersionParameter(HashMap<String, String> params){
		return params.containsKey(PARAM_VERSION);
	}

	boolean hasOSParameter(HashMap<String, String> params){
		return params.containsKey(PARAM_OS);
	}

	private boolean hasMethodParameter(HashMap<String, String> params){
		return params.containsKey(PARAM_METHOD);
	}


	private boolean hasEvenParameters(String[] s){
		boolean f = false;
		if((s != null) && (s.length != 0) && ((s.length % 2) == 0)){
			f = true;
		}
		return f;
	}

	public boolean isValid() {
		return valid;
	}

	public HashMap<String, String> getParams() {
		return params;
	}

	public String getMessage() {
		return message;
	}


	/**
	 * Checks to see if we have an implementation of the requested method
	 * @param s
	 * @return
	 */
	public boolean validMethod(String s) {
		System.out.println("valid method: ["+s+"]");
		boolean v = false;
		Method[] ms = RESTmethod.class.getDeclaredMethods();
		for(Method md:ms){
			if(md.getName().toLowerCase().equals(s.toLowerCase())){
				method = md;
				return true;
			}
		}
		return v;
	}

	public Method getMethod() {
		return method;
	}

}