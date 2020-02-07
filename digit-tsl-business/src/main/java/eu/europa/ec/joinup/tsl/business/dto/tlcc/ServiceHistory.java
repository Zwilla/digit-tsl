package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceHistoryInstance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "serviceHistoryInstance" })
@XmlRootElement(name = "ServiceHistory", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class ServiceHistory {

    @XmlElement(name = "ServiceHistoryInstance", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<ServiceHistoryInstance> serviceHistoryInstance;

    /**
     * Obtient la valeur de la propriete serviceHistoryInstance.
     *
     * @return possible object is {@link ServiceHistoryInstance }
     *
     */
    public List<ServiceHistoryInstance> getServiceHistoryInstance() {
        if (this.serviceHistoryInstance == null) {
            this.serviceHistoryInstance = new ArrayList<>();
        }
        return serviceHistoryInstance;
    }

    public void setServiceHistoryInstance(List<ServiceHistoryInstance> serviceHistoryInstance) {
        this.serviceHistoryInstance = serviceHistoryInstance;
    }

}
