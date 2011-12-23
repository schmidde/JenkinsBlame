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
	public List<Integer> getBuilds() throws IOException, JSONException{
		
		List<Integer> builds = new ArrayList<Integer>();
		
		json = new JSONObject("{\"actions\":[{},{},{},{}],\"description\":\"\",\"displayName\":\"Auto-B-Day\",\"name\":\"Auto-B-Day\",\"url\":\"http://rambow.it:8080/job/Auto-B-Day/\",\"buildable\":true,\"builds\":[{\"number\":22,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/22/\"},{\"number\":21,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/21/\"},{\"number\":20,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/20/\"}]}");
		
		for(int i = 0; i < json.getJSONArray("builds").length(); i++){
				builds.add(json.getJSONArray("builds").getJSONObject(i).getInt("number"));
		}
		return builds;
	}
	@Override
	public String getBuilder(int nr) throws IOException, JSONException{
		json = new JSONObject("{\"actions\":[{\"causes\":[{\"userName\":\"Andy Klay\"}]},{},{},{},{}]}");
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
	}
	@Override
	public String getLastBuilder() throws IOException, JSONException{
		
		json = new JSONObject("{\"actions\":[{\"causes\":[{\"userName\":\"Andy Klay\"}]},{},{},{},{}]}");
		return json.getJSONArray("actions").getJSONObject(0).getJSONArray("causes").getJSONObject(0).getString("userName");
	}
	@Override
	public String getColor() throws IOException, JSONException{

		json = new JSONObject("{\"color\":\"red\"}");
		return json.getString("color");
	}
	@Override
	public String getColor(int nr) throws IOException, JSONException{
		String color = null;
		json = new JSONObject("{\"color\":\"red\"}");
		color = json.getString("result");
		if(color.equals("SUCCESS")){
			return "blue";
		}
		else return "red";
	}
	public int getFirstBuild() throws IOException, JSONException{
		
		json = new JSONObject("{\"firstBuild\":{\"number\":20,\"url\":\"http://rambow.it:8080/job/Auto-B-Day/37/\"}}");
		return json.getJSONObject("firstBuild").getInt("number");
	}
	@Override
	public int getLastGoodBuild() throws IOException, JSONException{

		json = new JSONObject("{\"lastSuccessfulBuild\":{\"number\":21}}");
		return json.getJSONObject("lastSuccessfulBuild").getInt("number");
	}
	@Override
	public int getLastBadBuild() throws JSONException, IOException{

		json = new JSONObject("{\"lastFailedBuild\":{\"number\":22}}");
		return json.getJSONObject("lastFailedBuild").getInt("number");
	}
	@Override
	public long getLastTimeStamp() throws IOException, JSONException{

		json = new JSONObject("{\"timestamp\":1323541990370}");
		return json.getLong("timestamp");
	}
	@Override
	public long getTimeStamp(int nr) throws IOException, JSONException{
		
		json = new JSONObject("{\"timestamp\":1323541990370}");
		return json.getLong("timestamp");
	}
	@Override
	public JenkinsVO createJenkinsVO() {
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
}
