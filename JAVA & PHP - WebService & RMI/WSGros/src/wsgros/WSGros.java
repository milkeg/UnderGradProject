package wsgros;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WSGros {

	private Map<String,GrossisteInterface> grossistes;
	
	public WSGros() throws RemoteException, MalformedURLException
	{
		super();
		System.out.println("constructeur WSGros");
		System.out.println("gross : "+grossistes);
		grossistes = new HashMap<String,GrossisteInterface>();
		String[]args = {};
		chercherGrossistes(args);
	}
	
	private void chercherGrossistes(String[] args) throws RemoteException, MalformedURLException
	{
		if (System.getSecurityManager() == null) {
			System.setProperty("java.security.policy", "/Users/michaelgoletto/Documents/workspace/WSGros/src/security.policy");
			System.setSecurityManager(new RMISecurityManager());
		}
		String host;
		if (args.length == 0) host = "localhost:2099";
		else host = args[0];

		String[] names = Naming.list("rmi://" + host + "/");
		try{
			for (int i = 0; i < names.length; i++)
			{
				System.out.println(names[i]);
				GrossisteInterface gros = (GrossisteInterface) Naming.lookup(names[i]);
				grossistes.put(names[i], gros);
			}
		}
		catch (Exception e) { e.printStackTrace();}
	}
	
	public boolean commanderProduit(String ref, int quantite) throws RemoteException{
		Iterator<String> it = grossistes.keySet().iterator();
		boolean pres = false;
		while(it.hasNext()){
			String nom = it.next();
			GrossisteInterface gro = grossistes.get(nom);
			pres = gro.verifierPresence(ref);
			if(pres){
				gro.commanderProduit(ref, quantite);
				return true;
			}
		}
		return false;
	}
	
	public String verifierGrossiste(String ref) throws RemoteException {
		Iterator<String> it = grossistes.keySet().iterator();
		boolean pres = false;
		while(it.hasNext()){
			String nom = it.next();
			GrossisteInterface gro = grossistes.get(nom);
			pres = gro.verifierPresence(ref);
			if(pres){
				return "produit present chez "+nom;
			}else{
				System.out.println("Le produit n'est pas present chez le grossiste "+nom);
			}
		}
		if(!pres){
			return "le produit ne se trouve chez aucun grossiste";
		}
		return null;
	}
	
	/*public static void main(String[] args) throws RemoteException, MalformedURLException {
		WSGros ve = new WSGros();
	
		//Les grossistes
		Map<String,GrossisteInterface> gros = ve.getGrossistes();
		
		
		//Test grossiste
		System.out.println(ve.verifierGrossiste("Ben l'Oncle Soul"));
	}*/
	
}
