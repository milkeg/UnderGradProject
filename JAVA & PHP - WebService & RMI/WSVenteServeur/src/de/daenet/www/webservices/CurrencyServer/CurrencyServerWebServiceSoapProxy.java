package de.daenet.www.webservices.CurrencyServer;

public class CurrencyServerWebServiceSoapProxy implements de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceSoap {
  private String _endpoint = null;
  private de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceSoap currencyServerWebServiceSoap = null;
  
  public CurrencyServerWebServiceSoapProxy() {
    _initCurrencyServerWebServiceSoapProxy();
  }
  
  public CurrencyServerWebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initCurrencyServerWebServiceSoapProxy();
  }
  
  private void _initCurrencyServerWebServiceSoapProxy() {
    try {
      currencyServerWebServiceSoap = (new de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceLocator()).getCurrencyServerWebServiceSoap();
      if (currencyServerWebServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)currencyServerWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)currencyServerWebServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (currencyServerWebServiceSoap != null)
      ((javax.xml.rpc.Stub)currencyServerWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public de.daenet.www.webservices.CurrencyServer.CurrencyServerWebServiceSoap getCurrencyServerWebServiceSoap() {
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap;
  }
  
  public de.daenet.www.webservices.CurrencyServer.GetDataSetResponseGetDataSetResult getDataSet(java.lang.String provider) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getDataSet(provider);
  }
  
  public java.lang.String getXmlStream(java.lang.String provider) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getXmlStream(provider);
  }
  
  public double getCurrencyValue(java.lang.String provider, java.lang.String srcCurrency, java.lang.String dstCurrency) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getCurrencyValue(provider, srcCurrency, dstCurrency);
  }
  
  public double getDollarValue(java.lang.String provider, java.lang.String currency) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getDollarValue(provider, currency);
  }
  
  public java.lang.String getProviderDescription(java.lang.String provider) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getProviderDescription(provider);
  }
  
  public java.lang.String getProviderTimestamp(java.lang.String providerId, java.lang.String provider) throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getProviderTimestamp(providerId, provider);
  }
  
  public java.lang.String getProviderList() throws java.rmi.RemoteException{
    if (currencyServerWebServiceSoap == null)
      _initCurrencyServerWebServiceSoapProxy();
    return currencyServerWebServiceSoap.getProviderList();
  }
  
  
}