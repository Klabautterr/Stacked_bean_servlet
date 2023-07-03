package stacked_bs.bean;

import java.io.Serializable;
//Tobias Weiß
public class Assets implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String stockname;
	private int anzahl;
	private int buyin;

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getStockname() {
		return stockname;
	}

	public void setStockname(String stockname) {
		this.stockname = stockname;
	}

	public int getAnzahl() {
		return anzahl;
	}

	public void setAnzahl(int anzahl) {
		this.anzahl = anzahl;
	}

	public int getBuyin() {
		return buyin;
	}

	public void setBuyin(int buyin) {
		this.buyin = buyin;
	}

}
//Tobias Weiß