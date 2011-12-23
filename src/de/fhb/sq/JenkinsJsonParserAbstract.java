package de.fhb.sq;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.json.JSONException;

public abstract class JenkinsJsonParserAbstract extends HttpServlet implements JenkinsJsonParserInterface{
	public int getLastBuildNr(){return -1;}
	public int getLastBuildNr(String s){return -1;}
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
}
