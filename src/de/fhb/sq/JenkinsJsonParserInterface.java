package de.fhb.sq;

import java.util.List;
import java.io.IOException;
import org.json.JSONException;

public interface JenkinsJsonParserInterface{

	public int getLastBuildNr();
	public int getLastBuildNr(String s);
	public List getBuilds();
	public List getBuilds(String s);
	public String getBuilder(int nr);
	public String getLastBuilder();
	public String getColor();
	public String getColor(int nr);
	public int getFirstBuild();
	public int getLastGoodBuild();
	public int getLastBadBuild();
	public long getLastTimeStamp();
	public long getTimeStamp(int nr);
	public JenkinsVO createJenkinsVO();
}
