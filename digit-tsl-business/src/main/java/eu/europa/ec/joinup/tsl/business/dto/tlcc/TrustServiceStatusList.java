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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLTag" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SchemeInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TrustServiceProviderList" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "check", "tslTag", "schemeInformation", "trustServiceProviderList", "signature" })
@XmlRootElement(name = "TrustServiceStatusList", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TrustServiceStatusList {

    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlElement(name = "TSLTag", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLTag tslTag;
    @XmlElement(name = "SchemeInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SchemeInformation schemeInformation;
    @XmlElement(name = "TrustServiceProviderList", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TrustServiceProviderList trustServiceProviderList;
    @XmlElement(name = "Signature", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Signature signature;

    public List<Check> getCheck() {
        if (this.check == null) {
            this.check = new ArrayList<>();
        }
        return check;
    }

    public void setCheck(List<Check> check) {
        this.check = check;
    }

    public TSLTag getTSLTag() {
        return tslTag;
    }

    public void setTSLTag(TSLTag tslTag) {
        this.tslTag = tslTag;
    }

    public SchemeInformation getSchemeInformation() {
        return schemeInformation;
    }

    public void setSchemeInformation(SchemeInformation schemeInformation) {
        this.schemeInformation = schemeInformation;
    }

    public TrustServiceProviderList getTrustServiceProviderList() {
        return trustServiceProviderList;
    }

    public void setTrustServiceProviderList(TrustServiceProviderList trustServiceProviderList) {
        this.trustServiceProviderList = trustServiceProviderList;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

}
