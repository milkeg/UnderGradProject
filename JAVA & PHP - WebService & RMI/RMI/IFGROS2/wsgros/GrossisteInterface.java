package wsgros;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GrossisteInterface extends Remote {
	
	boolean verifierPresence(String ref) throws RemoteException;
	boolean commanderProduit(String ref, int quantite) throws RemoteException;
	
}
