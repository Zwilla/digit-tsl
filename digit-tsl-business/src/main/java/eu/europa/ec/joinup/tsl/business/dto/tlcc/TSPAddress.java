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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PostalAddresses" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ElectronicAddress" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "postalAddresses", "electronicAddress" })
@XmlRootElement(name = "TSPAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TSPAddress {

    @XmlElement(name = "PostalAddresses", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected PostalAddresses postalAddresses;
    @XmlElement(name = "ElectronicAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ElectronicAddress electronicAddress;

    /**
     * Obtient la valeur de la propriete postalAddresses.
     * 
     * @return possible object is {@link PostalAddresses }
     * 
     */
    public PostalAddresses getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Definit la valeur de la propriete postalAddresses.
     * 
     * @param value
     *            allowed object is {@link PostalAddresses }
     * 
     */
    public void setPostalAddresses(PostalAddresses value) {
        this.postalAddresses = value;
    }

    /**
     * Obtient la valeur de la propriete electronicAddress.
     * 
     * @return possible object is {@link ElectronicAddress }
     * 
     */
    public ElectronicAddress getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * Definit la valeur de la propriete electronicAddress.
     * 
     * @param value
     *            allowed object is {@link ElectronicAddress }
     * 
     */
    public void setElectronicAddress(ElectronicAddress value) {
        this.electronicAddress = value;
    }

}
