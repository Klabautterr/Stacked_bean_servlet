package stacked_bs.bean;

import java.io.Serializable;

public class Post implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String username;
	private String nachricht;
	private String link;
	private boolean loeschen;
	private String bildname;
	private byte[] bild;
	
	public boolean isLöschen() {
		return loeschen;
	}
	public void setLöschen(boolean löschen) {
		this.loeschen = löschen;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNachricht() {
		return nachricht;
	}
	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getBildname() {
		return bildname;
	}
	public void setBildname(String bildname) {
		this.bildname = bildname;
	}
	public byte[] getBild() {
		return bild;
	}
	public void setBild(byte[] bild) {
		this.bild = bild;
	}
	
}
