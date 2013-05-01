/**
 * WSBanqueService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsbanque;

public interface WSBanqueService extends javax.xml.rpc.Service {
    public java.lang.String getWSBanqueAddress();

    public wsbanque.WSBanque getWSBanque() throws javax.xml.rpc.ServiceException;

    public wsbanque.WSBanque getWSBanque(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
