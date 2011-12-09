package com.peirr.jrestserver.reactors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * This creates a response JSON String object given the response data
 * @author kurt
 *
 */
public class ResponseReactor {

	/**
	 * churns back the result set as JSON
	 * @param results
	 * @return
	 */
	public String create(ResultSet results,String msg){
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String,String> obj = new HashMap<String, String>();
		HashMap<String, Object> response = new HashMap<String, Object>();
		int total = 0;
		try {
			if(results != null){
				while (results.next()){
					int cs = results.getMetaData().getColumnCount();		    
					for(int i=1;i<=cs;i++){
						obj.put(results.getMetaData().getColumnName(i),results.getString(i));
					}
					data.add(obj);  
					total++;

				}
				response.put("response",Response.createDataResponse("request ok",total));
			}else{
				response.put("response",Response.createInfoResponse(msg));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.put("data", data);
		Gson gson = new Gson();
		return gson.toJson(response);
	}

	/**
	 * appends some more data to the resultset
	 * @param results
	 * @param data
	 * @return
	 */
	public String create(HashMap<String, String> data,ResponseType typ,String msg){
		HashMap<String, Object> response = new HashMap<String, Object>();
		if(typ.equals(ResponseType.DATA)){
			response.put("response",Response.createDataResponse("request ok",1));
		}else{
			response.put("response",Response.createInfoResponse(msg));
		}
		response.put("data", data);
		Gson gson = new Gson();
		return gson.toJson(response);
	}

	/**
	 * Creates a place holder response
	 * @return
	 */
	public String createEmptyResponse(){
		String desc = "Failed to process request, please try again later";		
		Response response = Response.createInfoResponse(desc);
		Gson gson = new Gson();
		return gson.toJson(response);
	}
}