package de.fhb.sq;

import java.io.IOException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JenkinsJsonParserServlet extends HttpServlet{
	
	private String serverUrl, jobName, generalURL, buildNrUrl, tree;
	private JSONObject json;
	private JenkinsDataCallerInterface jdc;
	
	public JenkinsJsonParserServlet(String serverUrl, String jobName){
		this.serverUrl = serverUrl;
		this.jobName = jobName;
	}
	
	public int getLastBuildNr() throws IOException, JSONException{
		
		String tree = "tree=builds[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONArray("builds").getJSONObject(0).getInt("number");
	}
	
	public String getLastBuilder() throws IOException, JSONException{
		//Todo
		String tree = "tree=actions[causes[userName]]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL() + tree);
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
	}
	
	public String getColor() throws IOException, JSONException{
		
		String tree= "tree=color";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getString("color");
	}
	
	public int getLastGoodBuild() throws IOException, JSONException{
		
		String tree = "tree=lastSuccessfulBuild[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONObject("lastSuccessfulBuild").getInt("number");
	}
	
	public int getLastBadBuild() throws JSONException, IOException{
		
		String tree = "tree=lastFailedBuild[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONObject("lastFailedBuild").getInt("number");
	}
	
	public long getLastTimeStamp() throws IOException, JSONException{
		
		String tree = "tree=timestamp";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL() + tree);
		return json.getLong("timestamp");
	}
	public JenkinsVO createJenkinsVO(){
		JenkinsVO jvo = new JenkinsVO();
		try {
			jvo.setColor(getColor());
			jvo.setLastBuilder(getLastBuilder());
			jvo.setLastBuildNumber(getLastBuildNr());
			jvo.setLastFailedBuildNumber(getLastBadBuild());
			jvo.setLastSuccessfulBuildNumber(getLastGoodBuild());
			jvo.setTimestamp(getLastTimeStamp());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jvo;
	}
	//generelle URL fuer JSON-Object des Jobs
	public String getGeneralURL() {
		
		return generalURL = serverUrl + "/job/" + jobName + "/api/json?";
	}
	//spezielle URL fuer das JSON-Object des letzten Builds
	public String getBuildNrURL() throws IOException, JSONException {
		return buildNrUrl = serverUrl + "/job/" + jobName + "/" + getLastBuildNr() + "/" + "/api/json?";
	}
	
}
