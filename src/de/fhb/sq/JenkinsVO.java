package de.fhb.sq;

import java.util.List;
/**
* @author Dennis Schmidt, Sebastian Graebitz
* @version 1.0
* 
* Klasse dient zur Erzeugung von Valueobjects
* Instanzen werden mit Daten vom Jenkins CI-Server gefüllt
*/
public class JenkinsVO {
	
	private int firstBuild;
	private List builds;
	private int lastBuild;
	private int lastFailedBuild;
	private int lastSuccessfulBuild;
	private String lastBuilder;
	private String color;
	private long timestamp;
	
	public JenkinsVO(){}

	public int getLastBuild() {
		return lastBuild;
	}

	public void setLastBuildNumber(int lastBuild) {
		this.lastBuild = lastBuild;
	}

	public int getLastFailedBuild() {
		return lastFailedBuild;
	}

	public void setLastFailedBuildNumber(int lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}

	public int getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	public void setLastSuccessfulBuildNumber(int lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}

	public String getLastBuilder() {
		return lastBuilder;
	}

	public void setLastBuilder(String lastBuilder) {
		this.lastBuilder = lastBuilder;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getFirstBuild() {
		return firstBuild;
	}

	public void setFirstBuildNumber(int firstBuild) {
		this.firstBuild = firstBuild;
	}

	public List<Integer> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Integer> builds) {
		this.builds = builds;
	}
	
	
}
