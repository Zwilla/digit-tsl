//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.tsl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for TrustServiceProviderListType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrustServiceProviderListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{https://uri.etsi.org/02231/v2#}TrustServiceProvider" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrustServiceProviderListType", propOrder = { "trustServiceProvider" })
public class TrustServiceProviderListTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "TrustServiceProvider", required = true)
    protected List<TSPTypeV5> trustServiceProvider;

    /**
     * Gets the value of the trustServiceProvider property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the trustServiceProvider property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTrustServiceProvider().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link TSPTypeV5 }
     * 
     * 
     */
    public List<TSPTypeV5> getTrustServiceProvider() {
        if (trustServiceProvider == null) {
            trustServiceProvider = new ArrayList<>();
        }
        return this.trustServiceProvider;
    }

}
