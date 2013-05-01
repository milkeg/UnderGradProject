package wsbanque;

public class WSBanqueProxy implements wsbanque.WSBanque {
  private String _endpoint = null;
  private wsbanque.WSBanque wSBanque = null;
  
  public WSBanqueProxy() {
    _initWSBanqueProxy();
  }
  
  public WSBanqueProxy(String endpoint) {
    _endpoint = endpoint;
    _initWSBanqueProxy();
  }
  
  private void _initWSBanqueProxy() {
    try {
      wSBanque = (new wsbanque.WSBanqueServiceLocator()).getWSBanque();
      if (wSBanque != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wSBanque)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wSBanque)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wSBanque != null)
      ((javax.xml.rpc.Stub)wSBanque)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public wsbanque.WSBanque getWSBanque() {
    if (wSBanque == null)
      _initWSBanqueProxy();
    return wSBanque;
  }
  
  public int verifierFondDispo(int numeroCompte, java.lang.String password, double montant) throws java.rmi.RemoteException{
    if (wSBanque == null)
      _initWSBanqueProxy();
    return wSBanque.verifierFondDispo(numeroCompte, password, montant);
  }
  
  public boolean effectuerPaiment(int numeroCompte, double montant) throws java.rmi.RemoteException{
    if (wSBanque == null)
      _initWSBanqueProxy();
    return wSBanque.effectuerPaiment(numeroCompte, montant);
  }
  
  public boolean addCompte(int numeroCompte, java.lang.String password, double solde) throws java.rmi.RemoteException{
    if (wSBanque == null)
      _initWSBanqueProxy();
    return wSBanque.addCompte(numeroCompte, password, solde);
  }
  
  
}