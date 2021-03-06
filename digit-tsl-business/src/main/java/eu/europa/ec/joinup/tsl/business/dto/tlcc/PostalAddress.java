package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}StreetAddress" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Locality" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}StateOrProvince" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PostalCode" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}CountryName" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="language" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "check", "streetAddress", "locality", "stateOrProvince", "postalCode", "countryName" })
@XmlRootElement(name = "PostalAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class PostalAddress {

    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlElement(name = "StreetAddress", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected StreetAddress streetAddress;
    @XmlElement(name = "Locality", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Locality locality;
    @XmlElement(name = "StateOrProvince", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected StateOrProvince stateOrProvince;
    @XmlElement(name = "PostalCode", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected PostalCode postalCode;
    @XmlElement(name = "CountryName", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected CountryName countryName;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;
    @XmlAttribute(name = "language", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String language;

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

    /**
     * Obtient la valeur de la propriete streetAddress.
     * 
     * @return possible object is {@link StreetAddress }
     * 
     */
    public StreetAddress getStreetAddress() {
        return streetAddress;
    }

    /**
     * Definit la valeur de la propriete streetAddress.
     * 
     * @param value
     *            allowed object is {@link StreetAddress }
     * 
     */
    public void setStreetAddress(StreetAddress value) {
        this.streetAddress = value;
    }

    /**
     * Obtient la valeur de la propriete locality.
     * 
     * @return possible object is {@link Locality }
     * 
     */
    public Locality getLocality() {
        return locality;
    }

    /**
     * Definit la valeur de la propriete locality.
     * 
     * @param value
     *            allowed object is {@link Locality }
     * 
     */
    public void setLocality(Locality value) {
        this.locality = value;
    }

    /**
     * Obtient la valeur de la propriete stateOrProvince.
     * 
     * @return possible object is {@link StateOrProvince }
     * 
     */
    public StateOrProvince getStateOrProvince() {
        return stateOrProvince;
    }

    /**
     * Definit la valeur de la propriete stateOrProvince.
     * 
     * @param value
     *            allowed object is {@link StateOrProvince }
     * 
     */
    public void setStateOrProvince(StateOrProvince value) {
        this.stateOrProvince = value;
    }

    /**
     * Obtient la valeur de la propriete postalCode.
     * 
     * @return possible object is {@link PostalCode }
     * 
     */
    public PostalCode getPostalCode() {
        return postalCode;
    }

    /**
     * Definit la valeur de la propriete postalCode.
     * 
     * @param value
     *            allowed object is {@link PostalCode }
     * 
     */
    public void setPostalCode(PostalCode value) {
        this.postalCode = value;
    }

    /**
     * Obtient la valeur de la propriete countryName.
     * 
     * @return possible object is {@link CountryName }
     * 
     */
    public CountryName getCountryName() {
        return countryName;
    }

    /**
     * Definit la valeur de la propriete countryName.
     * 
     * @param value
     *            allowed object is {@link CountryName }
     * 
     */
    public void setCountryName(CountryName value) {
        this.countryName = value;
    }

    /**
     * Obtient la valeur de la propriete index.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Definit la valeur de la propriete index.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

    /**
     * Obtient la valeur de la propriete language.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Definit la valeur de la propriete language.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLanguage(String value) {
        this.language = value;
    }

}
