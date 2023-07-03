package stacked_bs.bean;

import java.io.Serializable;
//Tobias Weiß
public class Stock implements Serializable{
	private static final long serialVersionUID = 1L;

	private double anzahlStock;

	public double getAnzahlStock() {
		return anzahlStock;
	}

	public void setAnzahlStock(double anzahlStocks) {
		this.anzahlStock = anzahlStocks;
	}
	
}
//Tobias Weiß