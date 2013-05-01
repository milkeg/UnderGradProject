package wsgros;

public class WSGrosProxy implements wsgros.WSGros {
  private String _endpoint = null;
  private wsgros.WSGros wSGros = null;
  
  public WSGrosProxy() {
    _initWSGrosProxy();
  }
  
  public WSGrosProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSGrosProxy();
  }
  
  private void _initWSGrosProxy() {
    try {
      wSGros = (new wsgros.WSGrosServiceLocator()).getWSGros();
      if (wSGros != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSGros)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSGros)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSGros != null)
      ((javax.xml.rpc.Stub)wSGros)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public wsgros.WSGros getWSGros() {
    if (wSGros == null)
      _initWSGrosProxy();
    return wSGros;
  }
  
  public boolean commanderProduit(java.lang.String ref, int quantite) throws java.rmi.RemoteException{
    if (wSGros == null)
      _initWSGrosProxy();
    return wSGros.commanderProduit(ref, quantite);
  }
  
  public java.lang.String verifierGrossiste(java.lang.String ref) throws java.rmi.RemoteException{
    if (wSGros == null)
      _initWSGrosProxy();
    return wSGros.verifierGrossiste(ref);
  }
  
  
}