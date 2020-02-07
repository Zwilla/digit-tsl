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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TrustServiceProvider" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "trustServiceProvider" })
@XmlRootElement(name = "TrustServiceProviderList", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TrustServiceProviderList {

    @XmlElement(name = "TrustServiceProvider", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<TrustServiceProvider> trustServiceProvider;

    /**
     * Obtient la valeur de la propriete trustServiceProvider.
     *
     * @return possible object is {@link TrustServiceProvider }
     *
     */
    public List<TrustServiceProvider> getTrustServiceProvider() {
        if (this.trustServiceProvider == null) {
            this.trustServiceProvider = new ArrayList<>();
        }
        return trustServiceProvider;
    }

    public void setTrustServiceProvider(List<TrustServiceProvider> trustServiceProvider) {
        this.trustServiceProvider = trustServiceProvider;
    }

}
