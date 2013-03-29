package Serveur;

import java.rmi.*;


public interface TamponInterface extends Remote{
	boolean inserer(long idClient, String message) throws RemoteException;
	boolean demandeInsertion(long idClient) throws RemoteException;
	String extraire() throws RemoteException;
	boolean retirerReservation(long id) throws RemoteException;
}
