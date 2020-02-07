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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}DocumentationReferences" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "identifier", "documentationReferences" })
@XmlRootElement(name = "PolicyIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class PolicyIdentifier {

    @XmlElement(name = "Identifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Identifier> identifier;
    @XmlElement(name = "DocumentationReferences", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<DocumentationReferences> documentationReferences;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    public List<Identifier> getIdentifier() {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public void setDocumentationReferences(List<DocumentationReferences> documentationReferences) {
        this.documentationReferences = documentationReferences;
    }

    public List<DocumentationReferences> getDocumentationReferences() {
        if (documentationReferences == null) {
            documentationReferences = new ArrayList<>();
        }
        return this.documentationReferences;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger value) {
        this.index = value;
    }

}
