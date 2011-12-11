package de.fhb.sq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import java.util.Date;

/**
 * Die Klasse holt von einem Jenkins CI Server XML oder JSON. 
 * Der Pfad des Servers und der Name des Projekts muss angegeben werden
 * 
 * @author Dennis Schmidt
 * @author Sebastian Gräbitz
 * 
 * @version 0.1
 * */
public class JenkinsDataCaller {
	
	private int buildNr;
	private String buildPath, buildJob, buildFarbe;
	private Date buildDate;
	
	public JenkinsDataCaller(String buildPath, String buildJob){
		this.buildPath = buildPath;
		this.buildJob = buildJob;
	}
	
	public void callXml(){
		//Todo
	}

	public void callJson(){
		//Todo
	}
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException{
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

}