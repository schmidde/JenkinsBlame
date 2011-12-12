package de.fhb.sq;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public interface JenkinsDataCallerInterface {
	
	public JSONObject callJson(String url) throws IOException, JSONException;

	public Object callXml(String url); 

}
