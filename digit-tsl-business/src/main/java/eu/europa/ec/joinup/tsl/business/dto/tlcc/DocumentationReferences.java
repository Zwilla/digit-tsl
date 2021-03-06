package eu.europa.ec.joinup.tsl.business.dto.tlcc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}DocumentationReference" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "checkOrDocumentationReference" })
@XmlRootElement(name = "DocumentationReferences", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class DocumentationReferences {

    @XmlElements({ @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker", type = Check.class),
            @XmlElement(name = "DocumentationReference", namespace = "http://www.etsi.org/19162/conformanceChecker", type = DocumentationReference.class) })
    protected List<Object> checkOrDocumentationReference;

    /**
     * Gets the value of the checkOrDocumentationReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the checkOrDocumentationReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getCheckOrDocumentationReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Check } {@link DocumentationReference }
     * 
     * 
     */
    public List<Object> getCheckOrDocumentationReference() {
        if (checkOrDocumentationReference == null) {
            checkOrDocumentationReference = new ArrayList<>();
        }
        return this.checkOrDocumentationReference;
    }

}
