package de.fhb.sq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;
import org.json.JSONObject;
import org.json.JSONException;

public class JenkinsJsonParserStub extends JenkinsJsonParserAbstract{

	private String serverUrl, jobName, generalURL, buildNrUrl, tree;
	private JSONObject json;
	
	public JenkinsJsonParserStub(String serverUrl, String jobName){
		this.serverUrl = serverUrl;
		this.jobName = jobName;
	}
	@Override
	public int getLastBuildNr(String s){
		int nr = 0;
		try {
			json = new JSONObject(s);//z.B. ("{\"builds\":[{\"number\":22},{\"number\":21},{\"number\":20}]}");
			nr = json.getJSONArray("builds").getJSONObject(0).getInt("number");
		} catch (JSONException e) {
			nr = -1;
		}
		return nr;
	}
	@Override
	public List<Integer> getBuilds(String s){
		
		List<Integer> builds = null;
		
		try {
			builds = new ArrayList<Integer>();
			json = new JSONObject(s);//z.b. ("{\"actions\":[{},{},{},{}],\"description\":\"\",\"displayName\":\"Auto-B-Day\",\"name\":\"Auto-B-Day\",\"url\":\"http://rambow.it:8080/job/Auto-B-Day/\",\"buildable\":true,\"builds\":[{\"number\":22,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/22/\"},{\"number\":21,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/21/\"},{\"number\":20,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/20/\"}]}");
			for(int i = 0; i < json.getJSONArray("builds").length(); i++){
				builds.add(json.getJSONArray("builds").getJSONObject(i).getInt("number"));
			}
		} catch (JSONException e) {
			builds.add(-1);
		}
		return builds;
	}
	@Override
	public String getBuilder(String jsonString){
		String s = null;
		try {
			json = new JSONObject(jsonString);
			s = json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getLastBuilder(){
		String s = null;
		try {
			json = new JSONObject("{\"actions\":[{\"causes\":[{\"userName\":\"Andy Klay\"}]},{},{},{},{}]}");
			 s = json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getColor(){
		String s = null;
		try {
			json = new JSONObject("{\"color\":\"red\"}");
			s = json.getString("color");
		} catch (JSONException e) {
			s = null;
		}
		return s;
	}
	@Override
	public String getColor(int nr){
		String color = null;
		try {
			json = new JSONObject("{\"color\":\"red\"}");
			color = json.getString("result");
			if(color.equals("SUCCESS")){
				color = "blue";
			}
			else color = "red";
		} catch (JSONException e) {
			color = null;
		}
		return color;
	}
	public int getFirstBuild(){
		int nr = 0;
		try {	
			json = new JSONObject("{\"firstBuild\":{\"number\":20,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/37/\"}}");
			nr = json.getJSONObject("firstBuild").getInt("number");
		} catch (JSONException e) {
			nr = 0;
		}
		return nr;
	}
	@Override
	public int getLastGoodBuild(){
		int nr = 0;
		try {
			json = new JSONObject("{\"lastSuccessfulBuild\":{\"number\":21}}");
			nr = json.getJSONObject("lastSuccessfulBuild").getInt("number");
		} catch (JSONException e) {
			nr = 0;
		}
		return nr;
	}
	@Override
	public int getLastBadBuild(){
		int nr = 0;
		try {
			json = new JSONObject("{\"lastFailedBuild\":{\"number\":22}}");
			nr = json.getJSONObject("lastFailedBuild").getInt("number");
		} catch (JSONException e) {
			nr = 0;
		}
		return nr;
	}
	@Override
	public long getLastTimeStamp(){
		long stamp = 0;
		try {
			json = new JSONObject("{\"timestamp\":1323541990370}");
			stamp = json.getLong("timestamp");
		} catch (JSONException e) {
			stamp = 0;
		}
		return stamp;
	}
	@Override
	public long getTimeStamp(int nr){
		long stamp = 0;
		try {
			json = new JSONObject("{\"timestamp\":1323541990370}");
			stamp = json.getLong("timestamp");
		} catch (JSONException e) {
			stamp = 0;
		}
		return stamp;
	}
	@Override
	public JenkinsVO createJenkinsVO() {
		JenkinsVO jvo = new JenkinsVO();

		jvo.setColor(getColor());
		jvo.setFirstBuildNumber(getFirstBuild());
		jvo.setBuilds(getBuilds());
		jvo.setLastBuilder(getLastBuilder());
		jvo.setLastBuildNumber(getLastBuildNr());
		jvo.setLastFailedBuildNumber(getLastBadBuild());
		jvo.setLastSuccessfulBuildNumber(getLastGoodBuild());
		jvo.setTimestamp(getLastTimeStamp());

		return jvo;
	}	
}
