/**
 * 
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

/**
 * @author dga
 *
 */
@Entity()
public class Client extends Persistent {
	private String login;
	private String password;
	private String email;
	private String validate;
	private List<Order> orders = new ArrayList<Order>();
	private long lastlogin;
	private boolean remember;
	private byte[] photo;
	

	public boolean equals(Object other){
		if(other != null && other instanceof Client)
			return getLogin().equals(((Client)other).getLogin());
		return false;
	}
	public int hashCode(){
		return getLogin().hashCode();
	}


	@Column(nullable = false, unique=true)
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(mappedBy="client")
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	
	public void setNowLastlogin() {
		Date now = new Date();
		this.setLastlogin(now.getTime());
	}
	public boolean isRemember() {
		return remember;
	}
	public void setRemember(boolean remember) {
		this.remember = remember;
	}
	public long getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(long lastlogin) {
		this.lastlogin = lastlogin;
	}
	
	@Lob
	@Column(length = 100000)
	// La taille de la colonne est nécessaire pour Derby
	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

}
