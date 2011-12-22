package de.fhb.sq;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Build {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private int nr;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String color;
	
	@Persistent
	private String builder;
	
	public Build(long timestamp, int nr, String color){
		this.date = date;
		this.nr = nr;
		this.color = color;
	}

	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBuilder() {
		return builder;
	}

	public void setBuilder(String builder) {
		this.builder = builder;
	}
	

}
