package de.fhb.sq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Date;
/**
 * @author Dennis Schmidt, Sebastian Graebitz
 * @version 1.0
 * 
 * Die Klasse holt von einem Jenkins CI Server XML oder JSON. 
 * Der Pfad des Servers und der Name des Projekts muss angegeben werden
 * */
public class JenkinsDataCaller implements JenkinsDataCallerInterface{
	
	private int buildNr;
	private String buildPath, buildJob, buildFarbe;
	private Date buildDate;
	
	public JenkinsDataCaller(){}
	public JenkinsDataCaller(String buildPath, String buildJob){
		this.buildPath = buildPath;
		this.buildJob = buildJob;
	}
	/**
	 * holt ein Json-Object aus einer URL
	 * @return Json-Object
	 */
	public JSONObject callJson(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	public Object callXml(String url){
		//Todo
		Object obj = null;
		return obj;
	}


	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

}