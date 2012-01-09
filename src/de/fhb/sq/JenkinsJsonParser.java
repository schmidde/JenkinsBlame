package de.fhb.sq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
* @author Dennis Schmidt, Sebastian Graebitz
* @version 1.0
* 
* Implementierung eines Parser fuer Json-Objekte von einem 
* Jenkins CI-Server
*/
public class JenkinsJsonParser implements JenkinsJsonParserInterface{
	
	private String serverUrl, jobName, generalURL, buildNrUrl, tree;
	private JSONObject json;
	private JenkinsDataCallerInterface jdc;
	
	public JenkinsJsonParser(String serverUrl, String jobName){
		this.serverUrl = serverUrl;
		this.jobName = jobName;
	}
	/**
	 * holt letzte Build-Nummer
	 * @return gibt eine Buildnummer oder (-1) im Fehlerfall zurueck
	 */
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
	/**
	 * holt alle Buildnummern vom CI-Server
	 * @return gibt Liste mit Integerwerten zurueck
	 */
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
	/**
	 * holt den Builder einer Buildnummer vom CI-Server
	 * @param eine Buildnummer
	 * @return gibt den Namen oder null im Fehlerfall zurueck
	 */
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
	/**
	 * holt den letzten Builder des Projects
	 * @return gibt den Builder oder null im Fehlerfall zurueck
	 */
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
	/**
	 * holt den aktuellen Status des letzten Build
	 * @return gibt blue oder red zurueck, null im Fehlerfall
	 */
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
	/**
	 * holt den Status eines bestimmten Build vom CI-Server
	 * @param Nummer des zu durchsuchenden Build
	 * @return gibt blue oder red zurueck, null im Fehlerfall
	 */
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
	/**
	 * holt die erste Buildnummer des Projekts
	 * @return die erste Buildnummer, (-1) im Fehlerfall
	 */
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
	/**
	 * holt den letzten erfolgreichen Build vom CI-Server
	 * @return letzter erfolgreicher Build, (-1) im Fehlerfall
	 */
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
	/**
	 * holt letzten felgeschlagenen Build vom CI-Server
	 * @return letzten felgeschlagenen Build, (-1) im Fehlerfall
	 */
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
	/**
	 * holt den Timestamp des letzten Build
	 * @return ein Timestamp
	 */
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
	/**
	 * holt den Timestamp eines bestimmten Build
	 * @param zu ermittelnde Buildnummer
	 * @return gibt gesuchten Timesamp zrueck
	 */
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
	/**
	 * erstellt und befuellt ein Jenkins ValueObject
	 * @return gibt ein befuelltes JenkinsVO zurueck
	 */
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
	/**
	 * setzt eine Rest-URL fuer den Jenkins CI-Server zusammen 
	 * @return Rest-URL
	 */
	public String getGeneralURL() {
		
		return generalURL = serverUrl + "/job/" + jobName + "/api/json?";
	}
	/**
	 * setzt eine Rest-URL fuer die letzte Buildnummer vom Jenkins CI-Server zusammen 
	 * @return Rest-URL
	 */
	public String getBuildNrURL() throws IOException, JSONException {
		return buildNrUrl = serverUrl + "/job/" + jobName + "/" + getLastBuildNr() + "/" + "/api/json?";
	}
	/**
	 * setzt eine Rest-URL fuer eine bestimmte Buildnummer vom Jenkins CI-Server zusammen
	 * @param zu durchsuchende Buildnummer 
	 * @return Rest-URL
	 */
	public String getBuildNrURL(int nr) throws IOException, JSONException {
		return buildNrUrl = serverUrl + "/job/" + jobName + "/" + nr + "/" + "/api/json?";
	}
	
}
