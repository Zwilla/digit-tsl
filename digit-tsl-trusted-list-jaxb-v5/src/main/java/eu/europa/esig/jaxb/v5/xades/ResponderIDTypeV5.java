//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.xades;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ResponderIDType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponderIDType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="ByName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ByKey" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponderIDType", propOrder = { "byName", "byKey" })
public class ResponderIDTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ByName")
    protected String byName;
    @XmlElement(name = "ByKey")
    protected byte[] byKey;

    /**
     * Gets the value of the byName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getByName() {
        return byName;
    }

    /**
     * Sets the value of the byName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setByName(String value) {
        this.byName = value;
    }

    /**
     * Gets the value of the byKey property.
     * 
     * @return possible object is byte[]
     */
    public byte[] getByKey() {
        return byKey;
    }

    /**
     * Sets the value of the byKey property.
     * 
     * @param value
     *            allowed object is byte[]
     */
    public void setByKey(byte[] value) {
        this.byKey = value;
    }

}
