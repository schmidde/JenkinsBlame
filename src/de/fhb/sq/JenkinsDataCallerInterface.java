package de.fhb.sq;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
/**
* @author Dennis Schmidt, Sebastian Graebitz
* @version 1.0
* 
* Interface zur Implementierung eines Caller
* fuer Json-Objekte aus einer URL
*/
public interface JenkinsDataCallerInterface {
	
	public JSONObject callJson(String url) throws IOException, JSONException;

	public Object callXml(String url); 

}
