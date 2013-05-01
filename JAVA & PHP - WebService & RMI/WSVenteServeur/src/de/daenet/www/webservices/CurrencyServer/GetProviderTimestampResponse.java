/**
 * GetProviderTimestampResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.daenet.www.webservices.CurrencyServer;

public class GetProviderTimestampResponse  implements java.io.Serializable {
    private java.lang.String getProviderTimestampResult;

    public GetProviderTimestampResponse() {
    }

    public GetProviderTimestampResponse(
           java.lang.String getProviderTimestampResult) {
           this.getProviderTimestampResult = getProviderTimestampResult;
    }


    /**
     * Gets the getProviderTimestampResult value for this GetProviderTimestampResponse.
     * 
     * @return getProviderTimestampResult
     */
    public java.lang.String getGetProviderTimestampResult() {
        return getProviderTimestampResult;
    }


    /**
     * Sets the getProviderTimestampResult value for this GetProviderTimestampResponse.
     * 
     * @param getProviderTimestampResult
     */
    public void setGetProviderTimestampResult(java.lang.String getProviderTimestampResult) {
        this.getProviderTimestampResult = getProviderTimestampResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetProviderTimestampResponse)) return false;
        GetProviderTimestampResponse other = (GetProviderTimestampResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getProviderTimestampResult==null && other.getGetProviderTimestampResult()==null) || 
             (this.getProviderTimestampResult!=null &&
              this.getProviderTimestampResult.equals(other.getGetProviderTimestampResult())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getGetProviderTimestampResult() != null) {
            _hashCode += getGetProviderTimestampResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetProviderTimestampResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", ">getProviderTimestampResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getProviderTimestampResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "getProviderTimestampResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
