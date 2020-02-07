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
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for UnsignedSignaturePropertiesType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnsignedSignaturePropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element name="CounterSignature" type="{https://uri.etsi.org/01903/v1.3.2#}CounterSignatureType"/&gt;
 *         &lt;element name="SignatureTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType"/&gt;
 *         &lt;element name="CompleteCertificateRefs" type="{https://uri.etsi.org/01903/v1.3.2#}CompleteCertificateRefsType"/&gt;
 *         &lt;element name="CompleteRevocationRefs" type="{https://uri.etsi.org/01903/v1.3.2#}CompleteRevocationRefsType"/&gt;
 *         &lt;element name="AttributeCertificateRefs" type="{https://uri.etsi.org/01903/v1.3.2#}CompleteCertificateRefsType"/&gt;
 *         &lt;element name="AttributeRevocationRefs" type="{https://uri.etsi.org/01903/v1.3.2#}CompleteRevocationRefsType"/&gt;
 *         &lt;element name="SigAndRefsTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType"/&gt;
 *         &lt;element name="RefsOnlyTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType"/&gt;
 *         &lt;element name="CertificateValues" type="{https://uri.etsi.org/01903/v1.3.2#}CertificateValuesType"/&gt;
 *         &lt;element name="RevocationValues" type="{https://uri.etsi.org/01903/v1.3.2#}RevocationValuesType"/&gt;
 *         &lt;element name="AttrAuthoritiesCertValues" type="{https://uri.etsi.org/01903/v1.3.2#}CertificateValuesType"/&gt;
 *         &lt;element name="AttributeRevocationValues" type="{https://uri.etsi.org/01903/v1.3.2#}RevocationValuesType"/&gt;
 *         &lt;element name="ArchiveTimeStamp" type="{https://uri.etsi.org/01903/v1.3.2#}XAdESTimeStampType"/&gt;
 *         &lt;any namespace='##other'/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnsignedSignaturePropertiesType", propOrder = { "counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs" })
public class UnsignedSignaturePropertiesTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElementRefs({ @XmlElementRef(name = "CounterSignature", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "AttrAuthoritiesCertValues", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "SignatureTimeStamp", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "CompleteCertificateRefs", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "AttributeRevocationRefs", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "AttributeCertificateRefs", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "ArchiveTimeStamp", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "CertificateValues", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "CompleteRevocationRefs", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "RefsOnlyTimeStamp", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "SigAndRefsTimeStamp", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "RevocationValues", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class),
            @XmlElementRef(name = "AttributeRevocationValues", namespace = "https://uri.etsi.org/01903/v1.3.2#", type = JAXBElement.class) })
    @XmlAnyElement(lax = true)
    protected List<Object> counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the
     * counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getCounterSignatureOrSignatureTimeStampOrCompleteCertificateRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link JAXBElement }{@code <}{@link CounterSignatureTypeV5 }{@code >}
     * {@link JAXBElement }{@code <}{@link CertificateValuesTypeV5 }{@code >} {@link JAXBElement }{@code <}{@link XAdESTimeStampTypeV5 }{@code >}
     * {@link JAXBElement }{@code <}{@link CompleteCertificateRefsTypeV5 }{@code >} {@link JAXBElement }{@code <}{@link CompleteRevocationRefsTypeV5
     * }{@code >} {@link Object } {@link JAXBElement }{@code <}{@link CompleteCertificateRefsTypeV5 }{@code >} {@link JAXBElement
     * }{@code <}{@link XAdESTimeStampTypeV5 }{@code >} {@link JAXBElement }{@code <}{@link CertificateValuesTypeV5 }{@code >} {@link JAXBElement
     * }{@code <}{@link CompleteRevocationRefsTypeV5 }{@code >} {@link JAXBElement }{@code <}{@link XAdESTimeStampTypeV5 }{@code >} {@link JAXBElement
     * }{@code <}{@link XAdESTimeStampTypeV5 }{@code >} {@link JAXBElement }{@code <}{@link RevocationValuesTypeV5 }{@code >} {@link JAXBElement
     * }{@code <}{@link RevocationValuesTypeV5 }{@code >}
     * 
     * 
     */
    public List<Object> getCounterSignatureOrSignatureTimeStampOrCompleteCertificateRefs() {
        if (counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs == null) {
            counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs = new ArrayList<>();
        }
        return this.counterSignatureOrSignatureTimeStampOrCompleteCertificateRefs;
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
