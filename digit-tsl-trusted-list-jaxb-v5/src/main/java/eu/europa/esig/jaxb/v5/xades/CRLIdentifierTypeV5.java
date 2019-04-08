/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.06 at 08:00:20 AM CET 
//

package eu.europa.esig.jaxb.v5.xades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for CRLIdentifierType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRLIdentifierType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IssueTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CRLIdentifierType", propOrder = { "issuer", "issueTime", "number" })
public class CRLIdentifierTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Issuer", required = true)
    protected String issuer;
    @XmlElement(name = "IssueTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issueTime;
    @XmlElement(name = "Number")
    protected BigInteger number;
    @XmlAttribute(name = "URI")
    @XmlSchemaType(name = "anyURI")
    protected String uri;

    /**
     * Gets the value of the issuer property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the issueTime property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getIssueTime() {
        return issueTime;
    }

    /**
     * Sets the value of the issueTime property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setIssueTime(XMLGregorianCalendar value) {
        this.issueTime = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return possible object is {@link BigInteger }
     * 
     */
    public BigInteger getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *            allowed object is {@link BigInteger }
     * 
     */
    public void setNumber(BigInteger value) {
        this.number = value;
    }

    /**
     * Gets the value of the uri property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getURI() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setURI(String value) {
        this.uri = value;
    }

}
