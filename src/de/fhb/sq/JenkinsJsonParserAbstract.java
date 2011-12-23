package de.fhb.sq;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.json.JSONException;

public abstract class JenkinsJsonParserAbstract extends HttpServlet implements JenkinsJsonParserInterface{
	public int getLastBuildNr(){return -1;}
	public int getLastBuildNr(String s){return -1;}
	public List getBuilds() throws IOException, JSONException{return null;}
	public String getBuilder(int nr) throws IOException, JSONException{return null;}
	public String getLastBuilder() throws IOException, JSONException{return null;}
	public String getColor() throws IOException, JSONException{return null;}
	public String getColor(int nr) throws IOException, JSONException{return null;}
	public int getFirstBuild() throws IOException, JSONException{return -1;}
	public int getLastGoodBuild() throws IOException, JSONException{return -1;}
	public int getLastBadBuild() throws JSONException, IOException{return -1;}
	public long getLastTimeStamp() throws IOException, JSONException{return -1;}
	public long getTimeStamp(int nr) throws IOException, JSONException{return -1;}
	public JenkinsVO createJenkinsVO(){return null;}
}
