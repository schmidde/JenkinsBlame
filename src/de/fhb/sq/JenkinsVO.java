package de.fhb.sq;

import java.util.List;

public class JenkinsVO {
	
	private int firstBuildNumber;
	private List builds;
	private int lastBuildNumber;
	private int lastFailedBuildNumber;
	private int lastSuccessfulBuildNumber;
	private String lastBuilder;
	private String color;
	private long timestamp;
	
	public JenkinsVO(){}

	public int getLastBuildNumber() {
		return lastBuildNumber;
	}

	public void setLastBuildNumber(int lastBuildNumber) {
		this.lastBuildNumber = lastBuildNumber;
	}

	public int getLastFailedBuildNumber() {
		return lastFailedBuildNumber;
	}

	public void setLastFailedBuildNumber(int lastFailedBuildNumber) {
		this.lastFailedBuildNumber = lastFailedBuildNumber;
	}

	public int getLastSuccessfulBuildNumber() {
		return lastSuccessfulBuildNumber;
	}

	public void setLastSuccessfulBuildNumber(int lastSuccessfulBuildNumber) {
		this.lastSuccessfulBuildNumber = lastSuccessfulBuildNumber;
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

	public int getFirstBuildNumber() {
		return firstBuildNumber;
	}

	public void setFirstBuildNumber(int firstBuildNumber) {
		this.firstBuildNumber = firstBuildNumber;
	}

	public List getBuilds() {
		return builds;
	}

	public void setBuilds(List builds) {
		this.builds = builds;
	}
	
	
}
