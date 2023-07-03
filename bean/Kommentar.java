//Jan Holtmann
package stacked_bs.bean;

import java.io.Serializable;

public class Kommentar implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String kommentar;
	private Long id;
	private Integer post_id;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getKommentar() {
		return kommentar;
	}
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPost_id() {
		return post_id;
	}
	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}
	
	

}
//Jan Holtmann