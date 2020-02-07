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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}OtherTSLPointer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "otherTSLPointer", "check" })
@XmlRootElement(name = "PointersToOtherTSL", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class PointersToOtherTSL {

    @XmlElement(name = "OtherTSLPointer", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<OtherTSLPointer> otherTSLPointer;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;

    public List<OtherTSLPointer> getOtherTSLPointer() {
        if (this.otherTSLPointer == null) {
            this.otherTSLPointer = new ArrayList<>();
        }
        return otherTSLPointer;
    }

    public void setOtherTSLPointer(List<OtherTSLPointer> otherTSLPointer) {
        this.otherTSLPointer = otherTSLPointer;
    }

    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<>();
        }
        return check;
    }

    public void setCheck(List<Check> check) {
        this.check = check;
    }

}
