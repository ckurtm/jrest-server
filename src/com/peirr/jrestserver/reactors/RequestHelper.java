package com.peirr.jrestserver.reactors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.peirr.jrestserver.Core;
import com.peirr.jrestserver.api.RESTmethod;

/**
 * Validates a request to note if we should proceed with processing
 * @author kurt
 *
 */
public class RequestHelper {
	String tag = RequestHelper.class.getSimpleName();

	private boolean                 valid;
	private HashMap<String, String> params = new HashMap<String, String>();
	private String 				    path;
	private String    				message;
	private Method                  method;

	private static String   PARAM_VERSION  = "vs";
	private static String   PARAM_METHOD   = "m";
	private static String   PARAM_OS       = "os";

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
	 *  - should have even parameters
	 *  - should have os param
	 *  - should have version param
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