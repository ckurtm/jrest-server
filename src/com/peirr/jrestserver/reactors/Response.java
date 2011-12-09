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

import java.util.Calendar;
import java.util.Date;



/**
 * Response object containing details about the response
 * @author kurt.mbanje ckurtm at gmail dot com
 *
 */
public class Response {
    private String       description;
    private String       stamp;
    private int          total;
    private ResponseType responsetype;
	
    public Response(String description, String stamp, int total,ResponseType responsetype) {
		this.description = description;
		this.stamp = stamp;
		this.total = total;
		this.responsetype = responsetype;
	}
  
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ResponseType getResponsetype() {
		return responsetype;
	}

	public void setResponsetype(ResponseType responsetype) {
		this.responsetype = responsetype;
	}

	@Override
	public String toString() {
		return "Response [description=" + description +	", stamp=" + stamp + ", total=" + total + ", responsetype=" + responsetype + "]";
	}
    
    /**
     * Creates an info response with the given message
     * @param m
     * @return
     */
    public static Response createInfoResponse(String m){
    	return  new Response(m, String.valueOf(dateToUnixTimeStamp(new Date())),0, ResponseType.INFO);
    }
    
    
    /**
     * Creates an data response with the given message
     * @param m
     * @return
     */
    public static Response createDataResponse(String m,int total){
    	return  new Response(m, String.valueOf(dateToUnixTimeStamp(new Date())),total, ResponseType.DATA);
    }
 
    /**
	 * Converts to a unix timestamp
	 * @param date
	 * @return
	 */
	public static int dateToUnixTimeStamp(Date date) { 
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (int) (c.getTimeInMillis() / 1000L);
	}
}
