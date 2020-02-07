//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.xades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import eu.europa.esig.jaxb.v5.xmldsig.CanonicalizationMethodTypeV5;

/**
 * <p>
 * Java class for GenericTimeStampType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenericTimeStampType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice minOccurs="0"&gt;
 *           &lt;element ref="{https://uri.etsi.org/01903/v1.3.2#}Include" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element ref="{https://uri.etsi.org/01903/v1.3.2#}ReferenceInfo" maxOccurs="unbounded"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}CanonicalizationMethod" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;element name="EncapsulatedTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}EncapsulatedPKIDataType"/&gt;
 *           &lt;element name="XMLTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}AnyType"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GenericTimeStampType", propOrder = { "include", "referenceInfo", "canonicalizationMethod", "encapsulatedTimeStampOrXMLTimeStamp" })
@XmlSeeAlso({ XAdESTimeStampTypeV5.class, OtherTimeStampTypeV5.class })
public abstract class GenericTimeStampTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Include")
    protected List<IncludeTypeV5> include;
    @XmlElement(name = "ReferenceInfo")
    protected List<ReferenceInfoTypeV5> referenceInfo;
    @XmlElement(name = "CanonicalizationMethod", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected CanonicalizationMethodTypeV5 canonicalizationMethod;
    @XmlElements({ @XmlElement(name = "EncapsulatedTimeStamp", type = EncapsulatedPKIDataTypeV5.class), @XmlElement(name = "XMLTimeStamp", type = AnyTypeV5.class) })
    protected List<Serializable> encapsulatedTimeStampOrXMLTimeStamp;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the include property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the include property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getInclude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link IncludeTypeV5 }
     * 
     * 
     */
    public List<IncludeTypeV5> getInclude() {
        if (include == null) {
            include = new ArrayList<>();
        }
        return this.include;
    }

    /**
     * Gets the value of the referenceInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the referenceInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getReferenceInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ReferenceInfoTypeV5 }
     * 
     * 
     */
    public List<ReferenceInfoTypeV5> getReferenceInfo() {
        if (referenceInfo == null) {
            referenceInfo = new ArrayList<>();
        }
        return this.referenceInfo;
    }

    /**
     * Gets the value of the canonicalizationMethod property.
     * 
     * @return possible object is {@link CanonicalizationMethodTypeV5 }
     * 
     */
    public CanonicalizationMethodTypeV5 getCanonicalizationMethod() {
        return canonicalizationMethod;
    }

    /**
     * Sets the value of the canonicalizationMethod property.
     * 
     * @param value
     *            allowed object is {@link CanonicalizationMethodTypeV5 }
     * 
     */
    public void setCanonicalizationMethod(CanonicalizationMethodTypeV5 value) {
        this.canonicalizationMethod = value;
    }

    /**
     * Gets the value of the encapsulatedTimeStampOrXMLTimeStamp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the encapsulatedTimeStampOrXMLTimeStamp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getEncapsulatedTimeStampOrXMLTimeStamp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link EncapsulatedPKIDataTypeV5 } {@link AnyTypeV5 }
     * 
     * 
     */
    public List<Serializable> getEncapsulatedTimeStampOrXMLTimeStamp() {
        if (encapsulatedTimeStampOrXMLTimeStamp == null) {
            encapsulatedTimeStampOrXMLTimeStamp = new ArrayList<>();
        }
        return this.encapsulatedTimeStampOrXMLTimeStamp;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setId(String value) {
        this.id = value;
    }

}
