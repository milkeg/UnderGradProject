/**
 * GetXmlStreamResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.daenet.www.webservices.CurrencyServer;

public class GetXmlStreamResponse  implements java.io.Serializable {
    private java.lang.String getXmlStreamResult;

    public GetXmlStreamResponse() {
    }

    public GetXmlStreamResponse(
           java.lang.String getXmlStreamResult) {
           this.getXmlStreamResult = getXmlStreamResult;
    }


    /**
     * Gets the getXmlStreamResult value for this GetXmlStreamResponse.
     * 
     * @return getXmlStreamResult
     */
    public java.lang.String getGetXmlStreamResult() {
        return getXmlStreamResult;
    }


    /**
     * Sets the getXmlStreamResult value for this GetXmlStreamResponse.
     * 
     * @param getXmlStreamResult
     */
    public void setGetXmlStreamResult(java.lang.String getXmlStreamResult) {
        this.getXmlStreamResult = getXmlStreamResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetXmlStreamResponse)) return false;
        GetXmlStreamResponse other = (GetXmlStreamResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getXmlStreamResult==null && other.getGetXmlStreamResult()==null) || 
             (this.getXmlStreamResult!=null &&
              this.getXmlStreamResult.equals(other.getGetXmlStreamResult())));
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
        if (getGetXmlStreamResult() != null) {
            _hashCode += getGetXmlStreamResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetXmlStreamResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", ">getXmlStreamResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getXmlStreamResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "getXmlStreamResult"));
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
