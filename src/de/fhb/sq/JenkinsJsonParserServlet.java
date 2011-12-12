package de.fhb.sq;

import java.io.IOException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JenkinsJsonParserServlet extends HttpServlet{
	
	private String serverUrl, jobName;
	private JSONObject json;
	private JenkinsDataCallerInterface jdc;
	
	public JenkinsJsonParserServlet(String serverUrl, String jobName){
		this.serverUrl = serverUrl;
		this.jobName = jobName;
	}
	
	public int getLastBuildNr() throws IOException, JSONException{
		
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(serverUrl + "/job/" + jobName + "/api/json?tree=builds[number]");
		return json.getJSONArray("builds").getJSONObject(0).getInt("number");
	}
	
	public String getLastBuilder() throws IOException, JSONException{
		//Todo
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(serverUrl + "/job/" + jobName + "/" + getLastBuildNr() + "/" + "/api/json?tree=actions[causes[userName]]");
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
		
		//return json.toString();
	}
}
