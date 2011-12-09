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
import com.peirr.jrestserver.reactors.ResponseReactor;


public class Rest extends HttpServlet {
	private static final long serialVersionUID = 5391547924121066491L;
	ResponseReactor rfact = new ResponseReactor();
	RESTmethod ffact;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("GET REQUEST");
		PrintWriter out = resp.getWriter();	
		String response = rfact.create(null,Core.UNKNOWN_ERROR);
		RequestHelper request = new RequestHelper(req.getPathInfo());
		if(request.isValid()){
			try {
				RESTmethod fc = new RESTmethod();
				Class<?> c = Class.forName(fc.getClass().getName());
				Object t = c.newInstance();			
				request.getMethod().setAccessible(true);
				response = (String) request.getMethod().invoke(t,request.getParams());
			} catch (IllegalArgumentException e) {
				response = rfact.create(null,e.getMessage());
			} catch (IllegalAccessException e) {
				response = rfact.create(null,e.getMessage());
			} catch (InvocationTargetException e) {
				response = rfact.create(null,e.getMessage());
			}catch (SQLException e) {
				response = rfact.create(null,e.getMessage());
			} catch (ClassNotFoundException e) {
				response = rfact.create(null,e.getMessage());
			} catch (InstantiationException e) {
				response = rfact.create(null,e.getMessage());
			}
		}else{
			response = rfact.create(null,request.getMessage());
		}
		
		resp.setContentType("text/plain");
		out.println(response);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)	throws ServletException, IOException {
		System.out.println("POST REQUEST");
		PrintWriter out = resp.getWriter();	
		String response = rfact.create(null,Core.UNKNOWN_ERROR);
		Enumeration<?> en = req.getParameterNames();  
		HashMap<String, String> params =  new HashMap<String, String>();
		while(en.hasMoreElements()){
			String param = (String) en.nextElement();
//			System.out.println("+param["+param+"]");
			params.put(param,req.getParameter(param));
		}
		RequestHelper request = new RequestHelper(params);
//		System.out.println("params: \n" + params);
		if(request.isValid()){
			try {
				RESTmethod fc = new RESTmethod();
				Class<?> c = Class.forName(fc.getClass().getName());
				Object t = c.newInstance();			
				request.getMethod().setAccessible(true);
				response = (String) request.getMethod().invoke(t,request.getParams());
			} catch (IllegalArgumentException e) {
				response = rfact.create(null,e.getMessage());
			} catch (IllegalAccessException e) {
				response = rfact.create(null,e.getMessage());
			} catch (InvocationTargetException e) {
				response = rfact.create(null,e.getMessage());
			} catch (SQLException e) {
				response = rfact.create(null,request.getMessage());
			} catch (ClassNotFoundException e) {
				response = rfact.create(null,e.getMessage());
			} catch (InstantiationException e) {
				response = rfact.create(null,e.getMessage());
			}
		}else{
			response = rfact.create(null,request.getMessage());
		}
		resp.setContentType("text/plain");
		out.println(response);

	}

	

}
