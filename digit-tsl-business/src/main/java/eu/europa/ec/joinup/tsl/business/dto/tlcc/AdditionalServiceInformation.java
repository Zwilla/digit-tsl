package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}URI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "uri" })
@XmlRootElement(name = "AdditionalServiceInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class AdditionalServiceInformation {

    @XmlElement(name = "URI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected URI uri;

    /**
     * Obtient la valeur de la propriete uri.
     *
     * @return possible object is {@link URI }
     */
    public URI getURI() {
        if (uri == null) {
            uri = new URI();
        }
        return uri;
    }

    /**
     * Definit la valeur de la propriete uri.
     *
     * @param value
     *            allowed object is {@link URI }
     */
    public void setURI(URI value) {
        uri = value;
    }

}
