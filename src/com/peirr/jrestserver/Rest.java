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

package com.peirr.jrestserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.peirr.jrestserver.api.RESTmethod;
import com.peirr.jrestserver.reactors.RequestHelper;
import com.peirr.jrestserver.reactors.ResponseFormat;
import com.peirr.jrestserver.reactors.ResponseReactor;

/**
 * This is the main entry point servlet for all REST requests to this application.
 * you dont need to change anything here unless you have some unique requirement.
 * @author kurt.mbanje ckurtm at gmail dot com
 *
 */
public class Rest extends HttpServlet {
	private static final long serialVersionUID = 5391547924121066491L;
	ResponseReactor rfact = new ResponseReactor();
	RESTmethod ffact;
	ResponseFormat  format = ResponseFormat.XML;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET REQUEST");
		PrintWriter out = resp.getWriter();
		RequestHelper request = new RequestHelper(req.getPathInfo());
		format = getFormat(request.getParams());
		String response = rfact.create(null,Core.UNKNOWN_ERROR,format);
		if(request.isValid()){
			System.out.println("valid request");
			try {
				RESTmethod fc = new RESTmethod();
				Class<?> c = Class.forName(fc.getClass().getName());
				Object t = c.newInstance();			
				request.getMethod().setAccessible(true);
				response = (String) request.getMethod().invoke(t,request.getParams());
				//System.out.println("RESPONSE IS: " + response);
			} catch (IllegalArgumentException e) {
				response = rfact.create(null,"ERROR:IllegalArgumentException: " + e.getMessage(),format);
			} catch (IllegalAccessException e) {
				response = rfact.create(null,"ERROR:IllegalAccessException: " + e.getMessage(),format);
			} catch (InvocationTargetException e) {
				response = rfact.create(null,"ERROR:InvocationTargetException: " + e.getMessage(),format);
			}catch (SQLException e) {
				response = rfact.create(null,"ERROR:SQLException: " + e.getMessage(),format);
			}catch (ClassNotFoundException e) {
				response = rfact.create(null,"ERROR:ClassNotFoundException: " + e.getMessage(),format);
			} catch (InstantiationException e) {
				response = rfact.create(null,"ERROR:InstantiationException: " + e.getMessage(),format);
			}
		}else{
			System.out.println("invalid request");
			response = rfact.create(null,request.getMessage(),format);
		}		
		if(format.equals(ResponseFormat.XML)){
			resp.setContentType("text/xml");
			out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			out.println("<request>");
			out.println(response);
			out.println("</request>");
		}else{
			resp.setContentType("text/plain");
			out.println(response);
		}
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("POST REQUEST");
		PrintWriter out = resp.getWriter();	


		Enumeration<?> en = req.getParameterNames();  
		HashMap<String, String> params =  new HashMap<String, String>();
		while(en.hasMoreElements()){
			String param = (String) en.nextElement();
			//			System.out.println("+param["+param+"]");
			params.put(param,req.getParameter(param));
		}
		RequestHelper request = new RequestHelper(params);
		format = getFormat(request.getParams());
		String response = rfact.create(null,Core.UNKNOWN_ERROR,format);

		//		System.out.println("params: \n" + params);
		if(request.isValid()){
			try {
				RESTmethod fc = new RESTmethod();
				Class<?> c = Class.forName(fc.getClass().getName());
				Object t = c.newInstance();			
				request.getMethod().setAccessible(true);
				response = (String) request.getMethod().invoke(t,request.getParams());
			} catch (IllegalArgumentException e) {
				response = rfact.create(null,e.getMessage(),format);
			} catch (IllegalAccessException e) {
				response = rfact.create(null,e.getMessage(),format);
			} catch (InvocationTargetException e) {
				response = rfact.create(null,e.getMessage(),format);
			} catch (SQLException e) {
				response = rfact.create(null,request.getMessage(),format);
			} catch (ClassNotFoundException e) {
				response = rfact.create(null,e.getMessage(),format);
			} catch (InstantiationException e) {
				response = rfact.create(null,e.getMessage(),format);
			}
		}else{
			response = rfact.create(null,request.getMessage(),format);
		}


		if(format.equals(ResponseFormat.XML)){
			resp.setContentType("text/xml");
			out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			out.println("<request>");
			out.println(response);
			out.println("</request>");
		}else{
			resp.setContentType("text/plain");
			out.println(response);

		}

	}


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
}
