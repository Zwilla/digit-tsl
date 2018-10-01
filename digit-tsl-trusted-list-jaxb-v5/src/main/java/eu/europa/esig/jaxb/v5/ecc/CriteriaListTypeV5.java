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


package eu.europa.esig.jaxb.v5.ecc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.esig.jaxb.v5.xades.AnyTypeV5;


/**
 * <p>Java class for CriteriaListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CriteriaListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="KeyUsage" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}KeyUsageType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="PolicySet" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}PoliciesListType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CriteriaList" type="{http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#}CriteriaListType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="otherCriteriaList" type="{http://uri.etsi.org/01903/v1.3.2#}AnyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="assert"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="all"/&gt;
 *             &lt;enumeration value="atLeastOne"/&gt;
 *             &lt;enumeration value="none"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CriteriaListType", propOrder = {
    "keyUsage",
    "policySet",
    "criteriaList",
    "description",
    "otherCriteriaList"
})
public class CriteriaListTypeV5
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "KeyUsage")
    protected List<KeyUsageTypeV5> keyUsage;
    @XmlElement(name = "PolicySet")
    protected List<PoliciesListTypeV5> policySet;
    @XmlElement(name = "CriteriaList")
    protected List<CriteriaListTypeV5> criteriaList;
    @XmlElement(name = "Description")
    protected String description;
    protected AnyTypeV5 otherCriteriaList;
    @XmlAttribute(name = "assert")
    protected String _assert;

    /**
     * Gets the value of the keyUsage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the keyUsage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyUsage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KeyUsageTypeV5 }
     * 
     * 
     */
    public List<KeyUsageTypeV5> getKeyUsage() {
        if (keyUsage == null) {
            keyUsage = new ArrayList<KeyUsageTypeV5>();
        }
        return this.keyUsage;
    }

    /**
     * Gets the value of the policySet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policySet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicySet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PoliciesListTypeV5 }
     * 
     * 
     */
    public List<PoliciesListTypeV5> getPolicySet() {
        if (policySet == null) {
            policySet = new ArrayList<PoliciesListTypeV5>();
        }
        return this.policySet;
    }

    /**
     * Gets the value of the criteriaList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the criteriaList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCriteriaList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CriteriaListTypeV5 }
     * 
     * 
     */
    public List<CriteriaListTypeV5> getCriteriaList() {
        if (criteriaList == null) {
            criteriaList = new ArrayList<CriteriaListTypeV5>();
        }
        return this.criteriaList;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the otherCriteriaList property.
     * 
     * @return
     *     possible object is
     *     {@link AnyTypeV5 }
     *     
     */
    public AnyTypeV5 getOtherCriteriaList() {
        return otherCriteriaList;
    }

    /**
     * Sets the value of the otherCriteriaList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyTypeV5 }
     *     
     */
    public void setOtherCriteriaList(AnyTypeV5 value) {
        this.otherCriteriaList = value;
    }

    /**
     * Gets the value of the assert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssert() {
        return _assert;
    }

    /**
     * Sets the value of the assert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssert(String value) {
        this._assert = value;
    }

}
