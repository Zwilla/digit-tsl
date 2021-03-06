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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SignedInfo" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}KeyInfo" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}SignatureValue" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "signedInfo", "keyInfo", "signatureValue", "check" })
@XmlRootElement(name = "Signature", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class Signature {

    @XmlElement(name = "SignedInfo", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SignedInfo signedInfo;
    @XmlElement(name = "KeyInfo", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected KeyInfo keyInfo;
    @XmlElement(name = "SignatureValue", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected SignatureValue signatureValue;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;

    /**
     * Obtient la valeur de la propriete signedInfo.
     * 
     * @return possible object is {@link SignedInfo }
     * 
     */
    public SignedInfo getSignedInfo() {
        return signedInfo;
    }

    /**
     * Definit la valeur de la propriete signedInfo.
     * 
     * @param value
     *            allowed object is {@link SignedInfo }
     * 
     */
    public void setSignedInfo(SignedInfo value) {
        this.signedInfo = value;
    }

    /**
     * Obtient la valeur de la propriete keyInfo.
     * 
     * @return possible object is {@link KeyInfo }
     * 
     */
    public KeyInfo getKeyInfo() {
        return keyInfo;
    }

    /**
     * Definit la valeur de la propriete keyInfo.
     * 
     * @param value
     *            allowed object is {@link KeyInfo }
     * 
     */
    public void setKeyInfo(KeyInfo value) {
        this.keyInfo = value;
    }

    /**
     * Obtient la valeur de la propriete signatureValue.
     * 
     * @return possible object is {@link SignatureValue }
     * 
     */
    public SignatureValue getSignatureValue() {
        return signatureValue;
    }

    /**
     * Definit la valeur de la propriete signatureValue.
     * 
     * @param value
     *            allowed object is {@link SignatureValue }
     * 
     */
    public void setSignatureValue(SignatureValue value) {
        this.signatureValue = value;
    }

    /**
     * Gets the value of the check property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the check property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Check }
     * 
     * 
     */
    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return this.check;
    }

}
