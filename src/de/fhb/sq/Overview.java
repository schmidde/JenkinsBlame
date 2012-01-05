package de.fhb.sq;

public class Overview {

	private int blau;
	private int rot;
	String name;
	
	public Overview(String name, int blau, int rot){
		this.name = name;
		this.blau = blau;
		this.rot = rot;
	}

	public int getBlau() {
		return blau;
	}

	public void setBlau(int blau) {
		this.blau = blau;
	}

	public int getRot() {
		return rot;
	}

	public void setRot(int rot) {
		this.rot = rot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
