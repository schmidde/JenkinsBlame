package de.fhb.sq;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Project {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private List<Build> builds;
	
	@Persistent
	private int lastFailedBuild;
	
	@Persistent
	private int lastSuccessfulBuild;

	public Project(String name){
		this.name = name;
	}
	
	public List<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	public Key getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public int getLastFailedBuild() {
		return lastFailedBuild;
	}

	public void setLastFailedBuild(int lastFailedBuild) {
		this.lastFailedBuild = lastFailedBuild;
	}

	public int getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	public void setLastSuccessfulBuild(int lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}
	
	
}
