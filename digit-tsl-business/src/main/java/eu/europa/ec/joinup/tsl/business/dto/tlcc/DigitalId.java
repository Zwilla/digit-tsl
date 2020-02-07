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
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}X509Certificate" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Other" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}X509SKI" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}X509SubjectName" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "x509Certificate", "other", "x509SKI", "x509SubjectName", "check" })
@XmlRootElement(name = "DigitalId", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class DigitalId {

    @XmlElement(name = "X509Certificate", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected X509Certificate x509Certificate;
    @XmlElement(name = "Other", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Other other;
    @XmlElement(name = "X509SKI", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected X509SKI x509SKI;
    @XmlElement(name = "X509SubjectName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected X509SubjectName x509SubjectName;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete x509Certificate.
     *
     * @return possible object is {@link X509Certificate }
     */
    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    /**
     * Definit la valeur de la propriete x509Certificate.
     *
     * @param value
     *            allowed object is {@link X509Certificate }
     */
    public void setX509Certificate(X509Certificate value) {
        x509Certificate = value;
    }

    /**
     * Obtient la valeur de la propriete other.
     *
     * @return possible object is {@link Other }
     */
    public Other getOther() {
        return other;
    }

    /**
     * Definit la valeur de la propriete other.
     *
     * @param value
     *            allowed object is {@link Other }
     */
    public void setOther(Other value) {
        other = value;
    }

    /**
     * Obtient la valeur de la propriete x509SKI.
     *
     * @return possible object is {@link X509SKI }
     */
    public X509SKI getX509SKI() {
        return x509SKI;
    }

    /**
     * Definit la valeur de la propriete x509SKI.
     *
     * @param value
     *            allowed object is {@link X509SKI }
     */
    public void setX509SKI(X509SKI value) {
        x509SKI = value;
    }

    /**
     * Obtient la valeur de la propriete x509SubjectName.
     *
     * @return possible object is {@link X509SubjectName }
     */
    public X509SubjectName getX509SubjectName() {
        return x509SubjectName;
    }

    /**
     * Definit la valeur de la propriete x509SubjectName.
     *
     * @param value
     *            allowed object is {@link X509SubjectName }
     */
    public void setX509SubjectName(X509SubjectName value) {
        x509SubjectName = value;
    }

    /**
     * Obtient la valeur de la propriete index.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Definit la valeur de la propriete index.
     *
     * @param value
     *            allowed object is {@link BigInteger }
     */
    public void setIndex(BigInteger value) {
        index = value;
    }

    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return check;
    }
}
