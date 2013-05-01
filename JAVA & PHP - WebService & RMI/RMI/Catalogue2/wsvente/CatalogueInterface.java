package wsvente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CatalogueInterface extends Remote {
	float consulterPrix(String ref) throws RemoteException;	
	int verifierDisponibilite(String ref) throws RemoteException;
	boolean ValiderAjout(String descr, String ref) throws RemoteException;
	boolean verifierPresence(String ref) throws RemoteException;
	boolean acheterProduit(String ref, int quantite) throws RemoteException;
	boolean approvisionner(String ref, int quantite) throws RemoteException;
	List<Produit> getListeProduitsParCategorie(String categorie) throws RemoteException;
	List<Produit> getAllProduits() throws RemoteException;
	List<Produit> getListeProduitsParType(String type) throws RemoteException;
	List<Categorie> getListeCategorie() throws RemoteException;
}
