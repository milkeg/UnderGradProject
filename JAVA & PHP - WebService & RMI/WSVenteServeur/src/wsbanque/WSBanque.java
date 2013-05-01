/**
 * WSBanque.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsbanque;

public interface WSBanque extends java.rmi.Remote {
    public int verifierFondDispo(int numeroCompte, java.lang.String password, double montant) throws java.rmi.RemoteException;
    public boolean effectuerPaiment(int numeroCompte, double montant) throws java.rmi.RemoteException;
    public boolean addCompte(int numeroCompte, java.lang.String password, double solde) throws java.rmi.RemoteException;
}
