package stacked_bs.bean;

import java.io.Serializable;

//Jonathan Vielwerth

public class Login implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String passwort;
	private boolean isProfi = true;
	private boolean offeneProfiAnfrage = false;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
	public boolean getIsProfi() {
		return isProfi;
	}
	public void setIsProfi(boolean isProfi) {
		this.isProfi = isProfi;
	}
	public boolean getOffeneProfiAnfrage() {
		return offeneProfiAnfrage;
	}
	public void setOffeneProfiAnfrage(boolean offeneProfiAnfrage) {
		this.offeneProfiAnfrage = offeneProfiAnfrage;
	}	

}
//Jonathan Vielwerth
