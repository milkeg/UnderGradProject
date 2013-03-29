package Serveur;

import java.util.Date;

public class Message {

	private static final int timeout = 10000;

	private long idClient;	
	private String message;
	private Date debut;
	private Date fin;

	public Message(long c, String message){
		idClient = c;
		this.message = message;
		debut = new Date(System.currentTimeMillis());
		fin = new Date(System.currentTimeMillis()+timeout);
	}

	public Message(long c){
		idClient = c;
		this.message = null;
		debut = new Date(System.currentTimeMillis());
		fin = new Date(System.currentTimeMillis()+timeout);
	}

	public long getIdClient() {
		return idClient;
	}

	public void setIdClient(long idClient) {
		this.idClient = idClient;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public String toString() {
		return "Message [idClient=" + idClient + ", message=" + message
				+ ", debut=" + debut + "]";
	}
}
