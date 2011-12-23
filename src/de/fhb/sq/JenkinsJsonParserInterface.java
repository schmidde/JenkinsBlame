package de.fhb.sq;

import java.util.List;
import java.io.IOException;
import org.json.JSONException;

public interface JenkinsJsonParserInterface{

	public int getLastBuildNr();
	public int getLastBuildNr(String s);
	public List getBuilds();
	public String getBuilder(int nr) throws IOException, JSONException;
	public String getLastBuilder() throws IOException, JSONException;
	public String getColor() throws IOException, JSONException;
	public String getColor(int nr) throws IOException, JSONException;
	public int getFirstBuild() throws IOException, JSONException;
	public int getLastGoodBuild() throws IOException, JSONException;
	public int getLastBadBuild() throws JSONException, IOException;
	public long getLastTimeStamp() throws IOException, JSONException;
	public long getTimeStamp(int nr) throws IOException, JSONException;
	public JenkinsVO createJenkinsVO();
}
