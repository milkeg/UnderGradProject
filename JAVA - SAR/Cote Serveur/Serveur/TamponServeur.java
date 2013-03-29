package Serveur;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class TamponServeur
{
	public static void main(String[] args) throws Exception{
            if(System.getSecurityManager() == null){
                System.setSecurityManager(new RMISecurityManager());
            }
            TamponInterfaceImpl tii = new TamponInterfaceImpl(10);
            Naming.rebind("rmi://localhost:"+args[0]+"/Tii", tii);
        }
}
