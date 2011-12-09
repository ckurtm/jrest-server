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

import java.util.HashMap;

/**
* This has all the methods that are exposed RESTFULLY by this server.
* All methods should have the same parameter type of hashmap<String,String>
* @author kurt.mbanje  ckurtm at gmail dot com
*/
public interface RESTinterface {
	   /**
	    * Returns the server version for the given client device
	    * @param appversion  - the application version
	    * @param os          - the os that the application is running on
	    * @return 
	    */
      public String version(HashMap<String, String> args);
            
      
      public String say(HashMap<String, String> args);
}
