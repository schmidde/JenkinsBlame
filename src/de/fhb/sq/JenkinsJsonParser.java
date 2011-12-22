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
	
	public int getLastBuildNr() throws IOException, JSONException{
		
		String tree = "tree=builds[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONArray("builds").getJSONObject(0).getInt("number");
	}
	@Override
	public List<Integer> getBuilds() throws IOException, JSONException{
		
		List<Integer> builds = new ArrayList<Integer>();
		jdc = new JenkinsDataCaller();
		
		json = jdc.callJson(getGeneralURL());
		
		for(int i = 0; i < json.getJSONArray("builds").length(); i++){
				builds.add(json.getJSONArray("builds").getJSONObject(i).getInt("number"));
		}
		return builds;
	}
	@Override
	public String getBuilder(int nr) throws IOException, JSONException{
		String tree = "tree=actions[causes[userName]]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL(nr) + tree);
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
	}
	@Override
	public String getLastBuilder() throws IOException, JSONException{
		String tree = "tree=actions[causes[userName]]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL() + tree);
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
	}
	@Override
	public String getColor() throws IOException, JSONException{
		
		String tree= "tree=color";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getString("color");
	}
	@Override
	public String getColor(int nr) throws IOException, JSONException{
		
		String tree= "tree=result", color = null;
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL(nr) + tree);
		color = json.getString("result");
		if(color.equals("SUCCESS")){
			return "blue";
		}
		else return "red";
	}
	@Override
	public int getFirstBuild() throws IOException, JSONException{
		
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL());
		return json.getJSONObject("firstBuild").getInt("number");
	}
	@Override
	public int getLastGoodBuild() throws IOException, JSONException{
		
		String tree = "tree=lastSuccessfulBuild[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONObject("lastSuccessfulBuild").getInt("number");
	}
	@Override
	public int getLastBadBuild() throws JSONException, IOException{
		
		String tree = "tree=lastFailedBuild[number]";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getGeneralURL() + tree);
		return json.getJSONObject("lastFailedBuild").getInt("number");
	}
	@Override
	public long getLastTimeStamp() throws IOException, JSONException{
		
		String tree = "tree=timestamp";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL() + tree);
		return json.getLong("timestamp");
	}
	@Override
	public long getTimeStamp(int nr) throws IOException, JSONException{
		
		String tree = "tree=timestamp";
		jdc = new JenkinsDataCaller();
		json = jdc.callJson(getBuildNrURL(nr) + tree);
		return json.getLong("timestamp");
	}
	@Override
	public JenkinsVO createJenkinsVO(){
		JenkinsVO jvo = new JenkinsVO();
		try {
			jvo.setColor(getColor());
			jvo.setFirstBuildNumber(getFirstBuild());
			jvo.setBuilds(getBuilds());
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
	public String getBuildNrURL(int nr) throws IOException, JSONException {
		return buildNrUrl = serverUrl + "/job/" + jobName + "/" + nr + "/" + "/api/json?";
	}
	
}
