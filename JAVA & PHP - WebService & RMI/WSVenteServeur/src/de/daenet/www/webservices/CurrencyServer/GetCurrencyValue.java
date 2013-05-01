/**
 * GetCurrencyValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.daenet.www.webservices.CurrencyServer;

public class GetCurrencyValue  implements java.io.Serializable {
    private java.lang.String provider;

    private java.lang.String srcCurrency;

    private java.lang.String dstCurrency;

    public GetCurrencyValue() {
    }

    public GetCurrencyValue(
           java.lang.String provider,
           java.lang.String srcCurrency,
           java.lang.String dstCurrency) {
           this.provider = provider;
           this.srcCurrency = srcCurrency;
           this.dstCurrency = dstCurrency;
    }


    /**
     * Gets the provider value for this GetCurrencyValue.
     * 
     * @return provider
     */
    public java.lang.String getProvider() {
        return provider;
    }


    /**
     * Sets the provider value for this GetCurrencyValue.
     * 
     * @param provider
     */
    public void setProvider(java.lang.String provider) {
        this.provider = provider;
    }


    /**
     * Gets the srcCurrency value for this GetCurrencyValue.
     * 
     * @return srcCurrency
     */
    public java.lang.String getSrcCurrency() {
        return srcCurrency;
    }


    /**
     * Sets the srcCurrency value for this GetCurrencyValue.
     * 
     * @param srcCurrency
     */
    public void setSrcCurrency(java.lang.String srcCurrency) {
        this.srcCurrency = srcCurrency;
    }


    /**
     * Gets the dstCurrency value for this GetCurrencyValue.
     * 
     * @return dstCurrency
     */
    public java.lang.String getDstCurrency() {
        return dstCurrency;
    }


    /**
     * Sets the dstCurrency value for this GetCurrencyValue.
     * 
     * @param dstCurrency
     */
    public void setDstCurrency(java.lang.String dstCurrency) {
        this.dstCurrency = dstCurrency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCurrencyValue)) return false;
        GetCurrencyValue other = (GetCurrencyValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.provider==null && other.getProvider()==null) || 
             (this.provider!=null &&
              this.provider.equals(other.getProvider()))) &&
            ((this.srcCurrency==null && other.getSrcCurrency()==null) || 
             (this.srcCurrency!=null &&
              this.srcCurrency.equals(other.getSrcCurrency()))) &&
            ((this.dstCurrency==null && other.getDstCurrency()==null) || 
             (this.dstCurrency!=null &&
              this.dstCurrency.equals(other.getDstCurrency())));
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
        if (getProvider() != null) {
            _hashCode += getProvider().hashCode();
        }
        if (getSrcCurrency() != null) {
            _hashCode += getSrcCurrency().hashCode();
        }
        if (getDstCurrency() != null) {
            _hashCode += getDstCurrency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCurrencyValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", ">getCurrencyValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provider");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "provider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("srcCurrency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "srcCurrency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dstCurrency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.daenet.de/webservices/CurrencyServer", "dstCurrency"));
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
