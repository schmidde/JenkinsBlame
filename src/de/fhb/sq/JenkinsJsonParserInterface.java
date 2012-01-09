package de.fhb.sq;

import java.util.List;
import java.io.IOException;
import org.json.JSONException;
/**
* @author Dennis Schmidt, Sebastian Graebitz
* @version 1.0
* 
* Interface zur Implementierung eines Parser
* fuer Json-Objekte von einem Jenkins CI-Server
*/
public interface JenkinsJsonParserInterface{

	public int getLastBuildNr();
	public List getBuilds();
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
