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
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeTerritory"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPName"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}URI"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeOperatorName"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "schemeTerritory", "tspName", "uri", "schemeOperatorName", "check" })
@XmlRootElement(name = "TakenOverBy", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TakenOverBy {

    @XmlElement(name = "SchemeTerritory", namespace = "http://www.etsi.org/19162/conformanceChecker", required = true)
    protected SchemeTerritory schemeTerritory;
    @XmlElement(name = "TSPName", namespace = "http://www.etsi.org/19162/conformanceChecker", required = true)
    protected TSPName tspName;
    @XmlElement(name = "URI", namespace = "http://www.etsi.org/19162/conformanceChecker", required = true)
    protected List<URI> uri;
    @XmlElement(name = "SchemeOperatorName", namespace = "http://www.etsi.org/19162/conformanceChecker", required = true)
    protected SchemeOperatorName schemeOperatorName;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;

    /**
     * Obtient la valeur de la propriete schemeTerritory.
     *
     * @return possible object is {@link SchemeTerritory }
     */
    public SchemeTerritory getSchemeTerritory() {
        return schemeTerritory;
    }

    /**
     * Definit la valeur de la propriete schemeTerritory.
     *
     * @param value
     *            allowed object is {@link SchemeTerritory }
     */
    public void setSchemeTerritory(SchemeTerritory value) {
        schemeTerritory = value;
    }

    /**
     * Obtient la valeur de la propriete tspName.
     *
     * @return possible object is {@link TSPName }
     */
    public TSPName getTSPName() {
        return tspName;
    }

    /**
     * Definit la valeur de la propriete tspName.
     *
     * @param value
     *            allowed object is {@link TSPName }
     */
    public void setTSPName(TSPName value) {
        tspName = value;
    }

    public List<URI> getURI() {
        if (uri == null) {
            uri = new ArrayList<>();
        }
        return uri;
    }

    public void setUri(List<URI> uri) {
        this.uri = uri;
    }

    /**
     * Obtient la valeur de la propriete schemeOperatorName.
     *
     * @return possible object is {@link SchemeOperatorName }
     */
    public SchemeOperatorName getSchemeOperatorName() {
        return schemeOperatorName;
    }

    /**
     * Definit la valeur de la propriete schemeOperatorName.
     *
     * @param value
     *            allowed object is {@link SchemeOperatorName }
     */
    public void setSchemeOperatorName(SchemeOperatorName value) {
        schemeOperatorName = value;
    }

    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return check;
    }

}
