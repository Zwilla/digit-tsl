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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceSupplyPoint" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "serviceSupplyPoint" })
@XmlRootElement(name = "ServiceSupplyPoints", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class ServiceSupplyPoints {

    @XmlElement(name = "ServiceSupplyPoint", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<ServiceSupplyPoint> serviceSupplyPoint;

    /**
     * Gets the value of the serviceSupplyPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the serviceSupplyPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getServiceSupplyPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ServiceSupplyPoint }
     * 
     * 
     */
    public List<ServiceSupplyPoint> getServiceSupplyPoint() {
        if (serviceSupplyPoint == null) {
            serviceSupplyPoint = new ArrayList<>();
        }
        return this.serviceSupplyPoint;
    }

}
