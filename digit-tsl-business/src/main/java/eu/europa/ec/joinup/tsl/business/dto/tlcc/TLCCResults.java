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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TrustServiceStatusList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "trustServiceStatusList" })
@XmlRootElement(name = "TLCCResults", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TLCCResults {

    @XmlElement(name = "TrustServiceStatusList", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TrustServiceStatusList trustServiceStatusList;

    /**
     * Obtient la valeur de la propriete trustServiceStatusList.
     * 
     * @return possible object is {@link TrustServiceStatusList }
     * 
     */
    public TrustServiceStatusList getTrustServiceStatusList() {
        return trustServiceStatusList;
    }

    /**
     * Definit la valeur de la propriete trustServiceStatusList.
     * 
     * @param value
     *            allowed object is {@link TrustServiceStatusList }
     * 
     */
    public void setTrustServiceStatusList(TrustServiceStatusList value) {
        this.trustServiceStatusList = value;
    }

}
