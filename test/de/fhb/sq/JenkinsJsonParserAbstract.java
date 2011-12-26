package de.fhb.sq;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.json.JSONException;

public abstract class JenkinsJsonParserAbstract extends HttpServlet implements JenkinsJsonParserInterface{
	
	/* Originalmethoden aus dem Interface JenkinsJsonInterface*/
	public int getLastBuildNr(){return -1;}
	public List getBuilds(){return null;}
	public String getBuilder(int nr){return null;}
	public String getLastBuilder(){return null;}
	public String getColor(){return null;}
	public String getColor(int nr){return null;}
	public int getFirstBuild(){return -1;}
	public int getLastGoodBuild(){return -1;}
	public int getLastBadBuild(){return -1;}
	public long getLastTimeStamp(){return -1;}
	public long getTimeStamp(int nr){return -1;}
	public JenkinsVO createJenkinsVO(){return null;}
	
	/* Methoden nehmen einen String an und erzeugen daraus ein JSONObject. 
	 * Der JenkinsDataCaller muss nicht mehr aufgerufen werden */
	public int getLastBuildNr(String jsonString){return -1;}
	public List getBuilds(String jsonString){return null;}
	public String getBuilder(String jsonString){return null;}
	public String getLastBuilder(String jsonString){return null;}
	public String getColor(String jsonString){return null;}
	public String getColor(int nr, String jsonString){return null;}
	public int getFirstBuild(String jsonString){return -1;}
	public int getLastGoodBuild(String jsonString){return -1;}
	public int getLastBadBuild(String jsonString){return -1;}
	public long getLastTimeStamp(String jsonString){return -1;}
	public long getTimeStamp(String jsonString){return -1;}
}
