package de.fhb.sq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JenkinsJsonParser implements JenkinsJsonParserInterface{
	
	private String serverUrl, jobName, generalURL, buildNrUrl, tree;
	private JSONObject json;
	private JenkinsDataCallerInterface jdc;
	
	public JenkinsJsonParser(String serverUrl, String jobName){
		this.serverUrl = serverUrl;
		this.jobName = jobName;
	}
	@Override
	public int getLastBuildNr(){
		
		int nr = 0;
		String tree = "tree=builds[number]";
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getGeneralURL() + tree);
			nr = json.getJSONArray("builds").getJSONObject(0).getInt("number");
		} catch (IOException e) {
			nr = -1;
		} catch (JSONException e) {
			nr = -1;
		}
		return nr;
	}
	@Override
	public List<Integer> getBuilds(){
		
		List<Integer> builds = new ArrayList<Integer>();
		jdc = new JenkinsDataCaller();
		
		try {
			json = jdc.callJson(getGeneralURL());
			for(int i = 0; i < json.getJSONArray("builds").length(); i++){
				builds.add(json.getJSONArray("builds").getJSONObject(i).getInt("number"));
			}	
		} catch (IOException e) {
			builds.add(-1);
		} catch (JSONException e) {
			builds.add(-1);
		}
		return builds;
	}
	@Override
	public String getBuilder(int nr){
		String tree = "tree=actions[causes[userName]]", s = null;
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getBuildNrURL(nr) + tree);
			s = json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
		} catch (IOException e) {
			s = null;
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getLastBuilder(){
		String tree = "tree=actions[causes[userName]]", s= null;
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getBuildNrURL() + tree);
			s = json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
		} catch (IOException e) {
			s = null;
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getColor(){
		
		String tree= "tree=color", s = null;
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getGeneralURL() + tree);
			s = json.getString("color");
		} catch (IOException e) {
			s = null;
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getColor(int nr){
		
		String tree= "tree=result", color = null;
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getBuildNrURL(nr) + tree);
			color = json.getString("result");
			if(color.equals("SUCCESS")){
				color = "blue";
			}
			else color = "red";
		} catch (IOException e) {
			color = null;
		} catch (JSONException e) {
			color = null;
		}
		return color;
	}
	@Override
	public int getFirstBuild(){
		int nr = 0;
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getGeneralURL());
			nr = json.getJSONObject("firstBuild").getInt("number");
		} catch (IOException e) {
			nr = -1;
		} catch (JSONException e) {
			nr = -1;
		}
		return nr;
	}
	@Override
	public int getLastGoodBuild(){
		int nr = 0;
		String tree = "tree=lastSuccessfulBuild[number]";
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getGeneralURL() + tree);
			nr = json.getJSONObject("lastSuccessfulBuild").getInt("number");
		} catch (IOException e) {
			nr = -1;
		} catch (JSONException e) {
			nr = -1;
		}
		return nr;
	}
	@Override
	public int getLastBadBuild(){
		int nr = 0;
		String tree = "tree=lastFailedBuild[number]";
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getGeneralURL() + tree);
			nr = json.getJSONObject("lastFailedBuild").getInt("number");
		} catch (IOException e) {
			nr = -1;
		} catch (JSONException e) {
			nr = -1;
		}
		return nr;
	}
	@Override
	public long getLastTimeStamp(){
		long stamp = 0;
		String tree = "tree=timestamp";
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getBuildNrURL() + tree);
			stamp = json.getLong("timestamp");
		} catch (IOException e) {
			stamp = -1;
		} catch (JSONException e) {
			stamp = -1;
		}
		return stamp;
	}
	@Override
	public long getTimeStamp(int nr){
		long stamp = 0;
		String tree = "tree=timestamp";
		jdc = new JenkinsDataCaller();
		try {
			json = jdc.callJson(getBuildNrURL(nr) + tree);
			stamp = json.getLong("timestamp");
		} catch (IOException e) {
			stamp = -1;
		} catch (JSONException e) {
			stamp = -1;
		}
		return stamp;
	}
	@Override
	public JenkinsVO createJenkinsVO(){
		JenkinsVO jvo = new JenkinsVO();
		int counter = 0;
		
		while(counter < 5){
			jvo.setColor(getColor());
			jvo.setFirstBuildNumber(getFirstBuild());
			jvo.setBuilds(getBuilds());
			jvo.setLastBuilder(getLastBuilder());
			jvo.setLastBuildNumber(getLastBuildNr());
			jvo.setLastFailedBuildNumber(getLastBadBuild());
			jvo.setLastSuccessfulBuildNumber(getLastGoodBuild());
			jvo.setTimestamp(getLastTimeStamp());
			counter++;
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
	public String getBuildNrURL(int nr) throws IOException, JSONException {
		return buildNrUrl = serverUrl + "/job/" + jobName + "/" + nr + "/" + "/api/json?";
	}
	
}
