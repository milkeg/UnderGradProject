/**
 * WSGros.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsgros;

public interface WSGros extends java.rmi.Remote {
    public boolean commanderProduit(java.lang.String ref, int quantite) throws java.rmi.RemoteException;
    public java.lang.String verifierGrossiste(java.lang.String ref) throws java.rmi.RemoteException;
}
