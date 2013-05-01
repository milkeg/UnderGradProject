/**
 * GetProviderListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.daenet.www.webservices.CurrencyServer;

public class GetProviderListResponse  implements java.io.Serializable {
    private java.lang.String getProviderListResult;

    public GetProviderListResponse() {
    }

    public GetProviderListResponse(
           java.lang.String getProviderListResult) {
           this.getProviderListResult = getProviderListResult;
    }


    /**
     * Gets the getProviderListResult value for this GetProviderListResponse.
     * 
     * @return getProviderListResult
     */
    public java.lang.String getGetProviderListResult() {
        return getProviderListResult;
    }


    /**
     * Sets the getProviderListResult value for this GetProviderListResponse.
     * 
     * @param getProviderListResult
     */
    public void setGetProviderListResult(java.lang.String getProviderListResult) {
        this.getProviderListResult = getProviderListResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetProviderListResponse)) return false;
        GetProviderListResponse other = (GetProviderListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getProviderListResult==null && other.getGetProviderListResult()==null) || 
             (this.getProviderListResult!=null &&
              this.getProviderListResult.equals(other.getGetProviderListResult())));
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
        if (getGetProviderListResult() != null) {
            _hashCode += getGetProviderListResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetProviderListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", ">getProviderListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getProviderListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "getProviderListResult"));
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
