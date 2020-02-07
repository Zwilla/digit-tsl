package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * 
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PolicyIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "policyIdentifier" })
@XmlRootElement(name = "PolicySet", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class PolicySet {

    @XmlElement(name = "PolicyIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<PolicyIdentifier> policyIdentifier;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Gets the value of the policyIdentifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the policyIdentifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getPolicyIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link PolicyIdentifier }
     * 
     * 
     */
    public List<PolicyIdentifier> getPolicyIdentifier() {
        if (policyIdentifier == null) {
            policyIdentifier = new ArrayList<>();
        }
        return this.policyIdentifier;
    }

    /**
     * Obtient la valeur de la propriete index.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Definit la valeur de la propriete index.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

}
