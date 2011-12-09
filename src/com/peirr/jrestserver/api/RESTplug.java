package com.peirr.jrestserver.api;

import java.util.HashMap;

/**
* This has all the methods that are exposed RESTFULLY by this server
* @author kurt
*
*/
public interface RESTplug {
	   /**
	    * Returns the server version for the given client device
	    * @param appversion  - the application version
	    * @param os          - the os that the application is running on
	    * @return 
	    */
      public String version(HashMap<String, String> args);
            
      
      public String say(HashMap<String, String> args);
}
