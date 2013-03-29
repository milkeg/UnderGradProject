package Client;

import java.rmi.Naming;
import java.rmi.RemoteException;

import Serveur.TamponInterface;

public class Consommateur{
	TamponInterface talon;
	String url; // rmi://machine:port/nom

	public Consommateur(String url){
		this.url = url;
		try {
			talon = (TamponInterface) Naming.lookup(url); 
		} catch (Exception e){
			e.printStackTrace();
		}	    
	}

	public String consommer(){
		try {
			return talon.extraire();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}

}
