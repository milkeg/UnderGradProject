/**
 * WSBanqueServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package wsbanque;

public class WSBanqueServiceLocator extends org.apache.axis.client.Service implements wsbanque.WSBanqueService {

    public WSBanqueServiceLocator() {
    }


    public WSBanqueServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WSBanqueServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WSBanque
    private java.lang.String WSBanque_address = "http://localhost:8080/WSBanque/services/WSBanque";

    public java.lang.String getWSBanqueAddress() {
        return WSBanque_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSBanqueWSDDServiceName = "WSBanque";

    public java.lang.String getWSBanqueWSDDServiceName() {
        return WSBanqueWSDDServiceName;
    }

    public void setWSBanqueWSDDServiceName(java.lang.String name) {
        WSBanqueWSDDServiceName = name;
    }

    public wsbanque.WSBanque getWSBanque() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSBanque_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSBanque(endpoint);
    }

    public wsbanque.WSBanque getWSBanque(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            wsbanque.WSBanqueSoapBindingStub _stub = new wsbanque.WSBanqueSoapBindingStub(portAddress, this);
            _stub.setPortName(getWSBanqueWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWSBanqueEndpointAddress(java.lang.String address) {
        WSBanque_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (wsbanque.WSBanque.class.isAssignableFrom(serviceEndpointInterface)) {
                wsbanque.WSBanqueSoapBindingStub _stub = new wsbanque.WSBanqueSoapBindingStub(new java.net.URL(WSBanque_address), this);
                _stub.setPortName(getWSBanqueWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WSBanque".equals(inputPortName)) {
            return getWSBanque();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://wsbanque", "WSBanqueService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://wsbanque", "WSBanque"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WSBanque".equals(portName)) {
            setWSBanqueEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
