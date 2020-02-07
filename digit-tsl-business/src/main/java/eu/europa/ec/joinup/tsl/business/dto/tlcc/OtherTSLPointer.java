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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceDigitalIdentities" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLLocation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}AdditionalInformation" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "serviceDigitalIdentities", "tslLocation", "check", "additionalInformation" })
@XmlRootElement(name = "OtherTSLPointer", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class OtherTSLPointer {

    @XmlElement(name = "ServiceDigitalIdentities", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceDigitalIdentities serviceDigitalIdentities;
    @XmlElement(name = "TSLLocation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLLocation tslLocation;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlElement(name = "AdditionalInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected AdditionalInformation additionalInformation;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete serviceDigitalIdentities.
     * 
     * @return possible object is {@link ServiceDigitalIdentities }
     * 
     */
    public ServiceDigitalIdentities getServiceDigitalIdentities() {
        return serviceDigitalIdentities;
    }

    /**
     * Definit la valeur de la propriete serviceDigitalIdentities.
     * 
     * @param value
     *            allowed object is {@link ServiceDigitalIdentities }
     * 
     */
    public void setServiceDigitalIdentities(ServiceDigitalIdentities value) {
        this.serviceDigitalIdentities = value;
    }

    /**
     * Obtient la valeur de la propriete tslLocation.
     * 
     * @return possible object is {@link TSLLocation }
     * 
     */
    public TSLLocation getTSLLocation() {
        return tslLocation;
    }

    /**
     * Definit la valeur de la propriete tslLocation.
     * 
     * @param value
     *            allowed object is {@link TSLLocation }
     * 
     */
    public void setTSLLocation(TSLLocation value) {
        this.tslLocation = value;
    }

    /**
     * Gets the value of the check property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the check property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Check }
     * 
     * 
     */
    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return this.check;
    }

    /**
     * Obtient la valeur de la propriete additionalInformation.
     * 
     * @return possible object is {@link AdditionalInformation }
     * 
     */
    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Definit la valeur de la propriete additionalInformation.
     * 
     * @param value
     *            allowed object is {@link AdditionalInformation }
     * 
     */
    public void setAdditionalInformation(AdditionalInformation value) {
        this.additionalInformation = value;
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
