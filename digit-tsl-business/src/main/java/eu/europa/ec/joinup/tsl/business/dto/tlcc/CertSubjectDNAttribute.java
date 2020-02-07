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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}AttributeOID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "attributeOID" })
@XmlRootElement(name = "CertSubjectDNAttribute", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class CertSubjectDNAttribute {

    @XmlElement(name = "AttributeOID", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected AttributeOID attributeOID;

    /**
     * Obtient la valeur de la propriete attributeOID.
     * 
     * @return possible object is {@link AttributeOID }
     * 
     */
    public AttributeOID getAttributeOID() {
        return attributeOID;
    }

    /**
     * Definit la valeur de la propriete attributeOID.
     * 
     * @param value
     *            allowed object is {@link AttributeOID }
     * 
     */
    public void setAttributeOID(AttributeOID value) {
        this.attributeOID = value;
    }

}
