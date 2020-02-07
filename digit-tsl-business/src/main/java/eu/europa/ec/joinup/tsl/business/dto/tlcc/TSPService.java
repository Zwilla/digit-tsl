package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.math.BigInteger;
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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceHistory" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "serviceInformation", "serviceHistory" })
@XmlRootElement(name = "TSPService", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TSPService {

    @XmlElement(name = "ServiceInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceInformation serviceInformation;
    @XmlElement(name = "ServiceHistory", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceHistory serviceHistory;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete serviceInformation.
     * 
     * @return possible object is {@link ServiceInformation }
     * 
     */
    public ServiceInformation getServiceInformation() {
        return serviceInformation;
    }

    /**
     * Definit la valeur de la propriete serviceInformation.
     * 
     * @param value
     *            allowed object is {@link ServiceInformation }
     * 
     */
    public void setServiceInformation(ServiceInformation value) {
        this.serviceInformation = value;
    }

    /**
     * Obtient la valeur de la propriete serviceHistory.
     * 
     * @return possible object is {@link ServiceHistory }
     * 
     */
    public ServiceHistory getServiceHistory() {
        return serviceHistory;
    }

    /**
     * Definit la valeur de la propriete serviceHistory.
     * 
     * @param value
     *            allowed object is {@link ServiceHistory }
     * 
     */
    public void setServiceHistory(ServiceHistory value) {
        this.serviceHistory = value;
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
