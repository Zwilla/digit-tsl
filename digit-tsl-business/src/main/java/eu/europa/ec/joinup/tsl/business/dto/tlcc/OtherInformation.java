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
 *       &lt;choice>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeOperatorName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeTerritory" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeTypeCommunityRules" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLType" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}MimeType" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "schemeOperatorName", "schemeTerritory", "schemeTypeCommunityRules", "tslType", "mimeType", "check" })
@XmlRootElement(name = "OtherInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class OtherInformation {

    @XmlElement(name = "SchemeOperatorName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeOperatorName schemeOperatorName;
    @XmlElement(name = "SchemeTerritory", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeTerritory schemeTerritory;
    @XmlElement(name = "SchemeTypeCommunityRules", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeTypeCommunityRules schemeTypeCommunityRules;
    @XmlElement(name = "TSLType", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLType tslType;
    @XmlElement(name = "MimeType", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected MimeType mimeType;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;

    public SchemeOperatorName getSchemeOperatorName() {
        return schemeOperatorName;
    }

    public void setSchemeOperatorName(SchemeOperatorName value) {
        this.schemeOperatorName = value;
    }

    public SchemeTerritory getSchemeTerritory() {
        return schemeTerritory;
    }

    public void setSchemeTerritory(SchemeTerritory value) {
        this.schemeTerritory = value;
    }

    public SchemeTypeCommunityRules getSchemeTypeCommunityRules() {
        return schemeTypeCommunityRules;
    }

    public void setSchemeTypeCommunityRules(SchemeTypeCommunityRules value) {
        this.schemeTypeCommunityRules = value;
    }

    public TSLType getTSLType() {
        return tslType;
    }

    public void setTSLType(TSLType value) {
        this.tslType = value;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeType value) {
        this.mimeType = value;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger value) {
        this.index = value;
    }

    public List<Check> getCheck() {
        if (this.check == null) {
            this.check = new ArrayList<>();
        }
        return check;
    }

    public void setCheck(List<Check> check) {
        this.check = check;
    }

}
