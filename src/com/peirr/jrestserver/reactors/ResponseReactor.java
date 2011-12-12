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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * This creates a response JSON String object given the response data
 * @author kurt.mbanje ckurtm at gmail dot com
 *
 */
public class ResponseReactor {

	/**
	 * churns back the result set as JSON
	 * @param results
	 * @return
	 */
	public String create(ResultSet results,String msg,ResponseFormat format){
		System.out.println("create [format:"+format+"]");
		if(format.equals(ResponseFormat.XML)){
			System.out.println("FORMAT is XML");
			return createXMLresult(results, msg);
		}else { // DEFAULT TO JSON
			System.out.println("DEFAULTING FORMAT to JSON");
			return createJSONresult(results, msg);
		}
	}
	
	
	/**
	 * Creates a JSON result from the supplied {@link ResultSet} 
	 * @param results - database query resultset
	 * @param msg     - response message to be attached to the result output as the description
	 * @return
	 */
	private String createJSONresult(ResultSet results,String msg){
		System.out.println("createJSONresult");
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		HashMap<String, Object> response = new HashMap<String, Object>();
		int total = 0;
		try {
			if(results != null){
				while (results.next()){
					int cs = results.getMetaData().getColumnCount();	
					HashMap<String,String> obj = new HashMap<String, String>();
					for(int i=1;i<=cs;i++){
						//System.out.println("data["+i+"]  " + results.getMetaData().getColumnName(i) + "=>" + results.getString(i));
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
			response.put("response",Response.createInfoResponse(e.getMessage()));
		}
		response.put("data", data);
		System.out.println("FORMAT is JSON");
		Gson gson = new Gson();
		//System.out.println("returning\n" + gson.toJson(response));
		return gson.toJson(response);
	}

	
	
	
	private String createXMLresult(ResultSet results,String msg){
		System.out.println("createXMLresult");
		StringBuffer xml     = new StringBuffer("");
		StringBuffer dataXML = new StringBuffer(" <data>\n");
		String respXML;		
		int total = 0;
		try {
			if(results != null){
				while (results.next()){
					dataXML.append("  <item>\n");
					int cs = results.getMetaData().getColumnCount();	
					for(int i=1;i<=cs;i++){
						String col = results.getMetaData().getColumnName(i);
						String val = results.getString(i);
						dataXML.append("   <" + col + ">");
						dataXML.append(val);
						dataXML.append("</" + col + ">\n");
					}
					dataXML.append("  </item>\n");
					total++;
				}
				Response r = Response.createDataResponse("request ok",total);
				respXML    = r.toXML();
			}else{
				Response r = Response.createInfoResponse(msg);
				respXML    = r.toXML();
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			Response r = Response.createInfoResponse(e.getMessage());
			respXML    = r.toXML();
		}
		dataXML.append(" </data>");
		xml.append(respXML + "\n");
		xml.append(dataXML + "\n");
		xml.append("");
		System.out.println("FORMAT is XML");
		return xml.toString();
	}
	/**
	 * appends some more data to the resultset
	 * @param results
	 * @param data
	 * @return

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
	}*/

	/**
	 * Creates a place holder response
	 * @return
	 */
	public String createEmptyJSONResponse(){
		String desc = "Failed to process request, please try again later";		
		Response response = Response.createInfoResponse(desc);
		Gson gson = new Gson();
		return gson.toJson(response);
	}
}