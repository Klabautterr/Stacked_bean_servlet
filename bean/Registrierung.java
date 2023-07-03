package stacked_bs.bean;

import java.io.Serializable;

//Jonathan Vielwerth

public class Registrierung implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String passwort;
	

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

}
//Jonathan Vielwerth
