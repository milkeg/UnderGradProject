/**
 * WSGrosService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsgros;

public interface WSGrosService extends javax.xml.rpc.Service {
    public java.lang.String getWSGrosAddress();

    public wsgros.WSGros getWSGros() throws javax.xml.rpc.ServiceException;

    public wsgros.WSGros getWSGros(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
