package stacked_bs.bean;

import java.io.Serializable;

public class AnlageOption implements Serializable {
	private static final long serialVersionUID = 1L;

	private String branche;
	private String land;
	private String name;
	private int preis;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLand() {
		return land;
	}
	public void setLand(String land) {
		this.land = land;
	}
	public String getBranche() {
		return branche;
	}
	public void setBranche(String branche) {
		this.branche = branche;
	}
	public int getPreis() {
		return preis;
	}
	public void setPreis(int preis) {
		this.preis = preis;
	}


}
