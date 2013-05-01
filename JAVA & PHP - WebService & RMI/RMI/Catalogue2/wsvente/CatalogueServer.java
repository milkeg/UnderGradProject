package wsvente;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;


public class CatalogueServer {
	
	public static void main(String[] args) throws RemoteException{
		final CatalogueInterface catalogue = new Catalogue("CatalogueXML/catalogue.xml");
		try {
			Naming.rebind("rmi://localhost:1099/Catalogue_"+((Catalogue)catalogue).getId(), catalogue);
			Runtime.getRuntime().addShutdownHook(new Thread() {
            	@Override
            	public void run()
            	{
            		try {
            			Naming.unbind("rmi://localhost:1099/Catalogue_"+((Catalogue)catalogue).getId());
            	   		System.out.println("Catalogue "+((Catalogue)catalogue).getId()+" retire du rmiregistry");
            	   	}catch(Exception e){
            	   		e.printStackTrace();
            	   	}
            	}
        	});
        	System.out.println("Catalogue "+((Catalogue)catalogue).getId()+" connecte au rmiregistry");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

} 
