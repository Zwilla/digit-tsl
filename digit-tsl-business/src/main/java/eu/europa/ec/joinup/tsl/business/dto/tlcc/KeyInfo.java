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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}X509Data" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "x509Data" })
@XmlRootElement(name = "KeyInfo", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class KeyInfo {

    @XmlElement(name = "X509Data", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected X509Data x509Data;

    /**
     * Obtient la valeur de la propriete x509Data.
     * 
     * @return possible object is {@link X509Data }
     * 
     */
    public X509Data getX509Data() {
        return x509Data;
    }

    /**
     * Definit la valeur de la propriete x509Data.
     * 
     * @param value
     *            allowed object is {@link X509Data }
     * 
     */
    public void setX509Data(X509Data value) {
        this.x509Data = value;
    }

}
