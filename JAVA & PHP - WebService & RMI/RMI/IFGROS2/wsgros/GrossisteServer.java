package wsgros;

import java.rmi.Naming;
import java.rmi.RemoteException;


public class GrossisteServer {
	
	public static void main(String[] args) throws RemoteException{
		final GrossisteInterface grossiste = new Grossiste("StockXML/stock.xml");
		try {
			Naming.rebind("rmi://localhost:2099/Stock_"+((Grossiste)grossiste).getId(), grossiste);
			Runtime.getRuntime().addShutdownHook(new Thread() {
            	@Override
            	public void run()
            	{
            		try {
            			Naming.unbind("rmi://localhost:2099/Stock_"+((Grossiste)grossiste).getId());
            	   		System.out.println("Grossiste "+((Grossiste)grossiste).getId()+" retire du rmiregistry");
            	   	}catch(Exception e){
            	   		e.printStackTrace();
            	   	}
            	}
        	});
			System.out.println("Grossiste "+((Grossiste)grossiste).getId()+" connecte au rmiregistry");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

} 
