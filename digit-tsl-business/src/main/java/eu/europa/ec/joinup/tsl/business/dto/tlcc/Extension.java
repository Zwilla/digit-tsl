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
 *       &lt;choice>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}AdditionalServiceInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ExpiredCertsRevocationInfo" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Qualifications" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TakenOverBy" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}Check" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/choice>
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
    "additionalServiceInformation",
    "expiredCertsRevocationInfo",
    "qualifications",
    "takenOverBy",
    "check"
})
@XmlRootElement(name = "Extension", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class Extension {

    @XmlElement(name = "AdditionalServiceInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected AdditionalServiceInformation additionalServiceInformation;
    @XmlElement(name = "ExpiredCertsRevocationInfo", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ExpiredCertsRevocationInfo expiredCertsRevocationInfo;
    @XmlElement(name = "Qualifications", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected Qualifications qualifications;
    @XmlElement(name = "TakenOverBy", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TakenOverBy takenOverBy;
    @XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Check> check;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete additionalServiceInformation.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalServiceInformation }
     *     
     */
    public AdditionalServiceInformation getAdditionalServiceInformation() {
        return additionalServiceInformation;
    }

    /**
     * Definit la valeur de la propriete additionalServiceInformation.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalServiceInformation }
     *     
     */
    public void setAdditionalServiceInformation(AdditionalServiceInformation value) {
        this.additionalServiceInformation = value;
    }

    /**
     * Obtient la valeur de la propriete expiredCertsRevocationInfo.
     * 
     * @return
     *     possible object is
     *     {@link ExpiredCertsRevocationInfo }
     *     
     */
    public ExpiredCertsRevocationInfo getExpiredCertsRevocationInfo() {
        return expiredCertsRevocationInfo;
    }

    /**
     * Definit la valeur de la propriete expiredCertsRevocationInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpiredCertsRevocationInfo }
     *     
     */
    public void setExpiredCertsRevocationInfo(ExpiredCertsRevocationInfo value) {
        this.expiredCertsRevocationInfo = value;
    }

    /**
     * Obtient la valeur de la propriete qualifications.
     * 
     * @return
     *     possible object is
     *     {@link Qualifications }
     *     
     */
    public Qualifications getQualifications() {
        return qualifications;
    }

    /**
     * Definit la valeur de la propriete qualifications.
     * 
     * @param value
     *     allowed object is
     *     {@link Qualifications }
     *     
     */
    public void setQualifications(Qualifications value) {
        this.qualifications = value;
    }

    /**
     * Obtient la valeur de la propriete takenOverBy.
     * 
     * @return
     *     possible object is
     *     {@link TakenOverBy }
     *     
     */
    public TakenOverBy getTakenOverBy() {
        return takenOverBy;
    }

    /**
     * Definit la valeur de la propriete takenOverBy.
     * 
     * @param value
     *     allowed object is
     *     {@link TakenOverBy }
     *     
     */
    public void setTakenOverBy(TakenOverBy value) {
        this.takenOverBy = value;
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
