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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPTradeName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPAddress" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPInformationURI" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPInformationExtensions" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tspName", "tspTradeName", "tspAddress", "tspInformationURI", "tspInformationExtensions" })
@XmlRootElement(name = "TSPInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TSPInformation {

    @XmlElement(name = "TSPName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPName tspName;
    @XmlElement(name = "TSPTradeName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPTradeName tspTradeName;
    @XmlElement(name = "TSPAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPAddress tspAddress;
    @XmlElement(name = "TSPInformationURI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPInformationURI tspInformationURI;
    @XmlElement(name = "TSPInformationExtensions", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPInformationExtensions tspInformationExtensions;

    /**
     * Obtient la valeur de la propriete tspName.
     * 
     * @return possible object is {@link TSPName }
     * 
     */
    public TSPName getTSPName() {
        return tspName;
    }

    /**
     * Definit la valeur de la propriete tspName.
     * 
     * @param value
     *            allowed object is {@link TSPName }
     * 
     */
    public void setTSPName(TSPName value) {
        this.tspName = value;
    }

    /**
     * Obtient la valeur de la propriete tspTradeName.
     * 
     * @return possible object is {@link TSPTradeName }
     * 
     */
    public TSPTradeName getTSPTradeName() {
        return tspTradeName;
    }

    /**
     * Definit la valeur de la propriete tspTradeName.
     * 
     * @param value
     *            allowed object is {@link TSPTradeName }
     * 
     */
    public void setTSPTradeName(TSPTradeName value) {
        this.tspTradeName = value;
    }

    /**
     * Obtient la valeur de la propriete tspAddress.
     * 
     * @return possible object is {@link TSPAddress }
     * 
     */
    public TSPAddress getTSPAddress() {
        return tspAddress;
    }

    /**
     * Definit la valeur de la propriete tspAddress.
     * 
     * @param value
     *            allowed object is {@link TSPAddress }
     * 
     */
    public void setTSPAddress(TSPAddress value) {
        this.tspAddress = value;
    }

    /**
     * Obtient la valeur de la propriete tspInformationURI.
     * 
     * @return possible object is {@link TSPInformationURI }
     * 
     */
    public TSPInformationURI getTSPInformationURI() {
        return tspInformationURI;
    }

    /**
     * Definit la valeur de la propriete tspInformationURI.
     * 
     * @param value
     *            allowed object is {@link TSPInformationURI }
     * 
     */
    public void setTSPInformationURI(TSPInformationURI value) {
        this.tspInformationURI = value;
    }

    /**
     * Obtient la valeur de la propriete tspInformationExtensions.
     * 
     * @return possible object is {@link TSPInformationExtensions }
     * 
     */
    public TSPInformationExtensions getTSPInformationExtensions() {
        return tspInformationExtensions;
    }

    /**
     * Definit la valeur de la propriete tspInformationExtensions.
     * 
     * @param value
     *            allowed object is {@link TSPInformationExtensions }
     * 
     */
    public void setTSPInformationExtensions(TSPInformationExtensions value) {
        this.tspInformationExtensions = value;
    }

}
