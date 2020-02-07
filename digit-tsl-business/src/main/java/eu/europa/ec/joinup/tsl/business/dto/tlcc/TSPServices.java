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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPService" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tspService" })
@XmlRootElement(name = "TSPServices", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TSPServices {

    @XmlElement(name = "TSPService", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<TSPService> tspService;

    /**
     * Obtient la valeur de la propriete tspService.
     *
     * @return possible object is {@link TSPService }
     *
     */
    public List<TSPService> getTSPService() {
        if (tspService == null) {
            this.tspService = new ArrayList<>();
        }
        return tspService;
    }

    /**
     * Definit la valeur de la propriete tspService.
     *
     * @param value
     *            allowed object is {@link TSPService }
     *
     */
    public void setTSPService(List<TSPService> value) {
        this.tspService = value;
    }

}
