package Client;

import java.rmi.*;

import Serveur.TamponInterface;

public class Producteur{
	TamponInterface talon;
	String url; // rmi://machine:port/nom, Tii dans notre cas
	long id;

	public Producteur(String url){
		this.url = url;
		this.id = ((long)(Math.random()*1000))*System.currentTimeMillis();
		try {
			talon = (TamponInterface) Naming.lookup(url); 
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public boolean produire(String message){
		try {
			return talon.inserer(id, message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean demandeInserer(){
		try {
			return talon.demandeInsertion(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean retirerReservation() {
		try {
			return talon.retirerReservation(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

}
