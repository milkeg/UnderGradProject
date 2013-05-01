/**
 * CurrencyServerWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.daenet.www.webservices.CurrencyServer;

public interface CurrencyServerWebServiceSoap extends java.rmi.Remote {

    /**
     * Retrieves the currencies in a Dataset for the specified provider.
     */
    public de.daenet.www.webservices.CurrencyServer.GetDataSetResponseGetDataSetResult getDataSet(java.lang.String provider) throws java.rmi.RemoteException;

    /**
     * Retrieves the currencies in the XML format for the specified
     * provider.
     */
    public java.lang.String getXmlStream(java.lang.String provider) throws java.rmi.RemoteException;

    /**
     * Calculates the currency factor for specified currencies. Example:
     * dstCurrency = f * srcCurrency
     */
    public double getCurrencyValue(java.lang.String provider, java.lang.String srcCurrency, java.lang.String dstCurrency) throws java.rmi.RemoteException;

    /**
     * Retrieves the dollar value of the specified currency.
     */
    public double getDollarValue(java.lang.String provider, java.lang.String currency) throws java.rmi.RemoteException;

    /**
     * Retrieves the description of the specified provider.
     */
    public java.lang.String getProviderDescription(java.lang.String provider) throws java.rmi.RemoteException;

    /**
     * Retrieves the timestamp of the specified provider.
     */
    public java.lang.String getProviderTimestamp(java.lang.String providerId, java.lang.String provider) throws java.rmi.RemoteException;

    /**
     * Retrieves the list of all supported currency providers.
     */
    public java.lang.String getProviderList() throws java.rmi.RemoteException;
}
