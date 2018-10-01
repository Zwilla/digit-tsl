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
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceDigitalIdentities" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSLLocation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}AdditionalInformation" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "serviceDigitalIdentities",
    "tslLocation",
    "check",
    "additionalInformation"
})
@XmlRootElement(name = "OtherTSLPointer", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class OtherTSLPointer {

    @XmlElement(name = "ServiceDigitalIdentities", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ServiceDigitalIdentities serviceDigitalIdentities;
    @XmlElement(name = "TSLLocation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSLLocation tslLocation;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlElement(name = "AdditionalInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected AdditionalInformation additionalInformation;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete serviceDigitalIdentities.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDigitalIdentities }
     *     
     */
    public ServiceDigitalIdentities getServiceDigitalIdentities() {
        return serviceDigitalIdentities;
    }

    /**
     * Definit la valeur de la propriete serviceDigitalIdentities.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDigitalIdentities }
     *     
     */
    public void setServiceDigitalIdentities(ServiceDigitalIdentities value) {
        this.serviceDigitalIdentities = value;
    }

    /**
     * Obtient la valeur de la propriete tslLocation.
     * 
     * @return
     *     possible object is
     *     {@link TSLLocation }
     *     
     */
    public TSLLocation getTSLLocation() {
        return tslLocation;
    }

    /**
     * Definit la valeur de la propriete tslLocation.
     * 
     * @param value
     *     allowed object is
     *     {@link TSLLocation }
     *     
     */
    public void setTSLLocation(TSLLocation value) {
        this.tslLocation = value;
    }

    /**
     * Gets the value of the check property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the check property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheck().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Check }
     * 
     * 
     */
    public List<Check> getCheck() {
        if (check == null) {
            check = new ArrayList<Check>();
        }
        return this.check;
    }

    /**
     * Obtient la valeur de la propriete additionalInformation.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalInformation }
     *     
     */
    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Definit la valeur de la propriete additionalInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalInformation }
     *     
     */
    public void setAdditionalInformation(AdditionalInformation value) {
        this.additionalInformation = value;
    }

    /**
     * Obtient la valeur de la propriete index.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIndex() {
        return index;
    }

    /**
     * Definit la valeur de la propriete index.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIndex(BigInteger value) {
        this.index = value;
    }

}
