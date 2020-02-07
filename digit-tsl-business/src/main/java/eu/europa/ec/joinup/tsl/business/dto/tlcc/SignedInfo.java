package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Reference" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Canonicalization" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "reference", "canonicalization" })
@XmlRootElement(name = "SignedInfo", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class SignedInfo {

    @XmlElement(name = "Reference", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Reference reference;
    @XmlElement(name = "Canonicalization", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Canonicalization canonicalization;

    /**
     * Obtient la valeur de la propriete reference.
     * 
     * @return possible object is {@link Reference }
     * 
     */
    public Reference getReference() {
        return reference;
    }

    /**
     * Definit la valeur de la propriete reference.
     * 
     * @param value
     *            allowed object is {@link Reference }
     * 
     */
    public void setReference(Reference value) {
        this.reference = value;
    }

    /**
     * Obtient la valeur de la propriete canonicalization.
     * 
     * @return possible object is {@link Canonicalization }
     * 
     */
    public Canonicalization getCanonicalization() {
        return canonicalization;
    }

    /**
     * Definit la valeur de la propriete canonicalization.
     * 
     * @param value
     *            allowed object is {@link Canonicalization }
     * 
     */
    public void setCanonicalization(Canonicalization value) {
        this.canonicalization = value;
    }

}
