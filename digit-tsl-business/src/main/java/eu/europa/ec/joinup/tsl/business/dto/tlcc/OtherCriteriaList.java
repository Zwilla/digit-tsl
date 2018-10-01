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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}CertSubjectDNAttribute" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ExtendedKeyUsage" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "certSubjectDNAttribute",
    "extendedKeyUsage"
})
@XmlRootElement(name = "otherCriteriaList", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class OtherCriteriaList {

    @XmlElement(name = "CertSubjectDNAttribute", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected CertSubjectDNAttribute certSubjectDNAttribute;
    @XmlElement(name = "ExtendedKeyUsage", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected ExtendedKeyUsage extendedKeyUsage;

    /**
     * Obtient la valeur de la propriete certSubjectDNAttribute.
     * 
     * @return
     *     possible object is
     *     {@link CertSubjectDNAttribute }
     *     
     */
    public CertSubjectDNAttribute getCertSubjectDNAttribute() {
        return certSubjectDNAttribute;
    }

    /**
     * Definit la valeur de la propriete certSubjectDNAttribute.
     * 
     * @param value
     *     allowed object is
     *     {@link CertSubjectDNAttribute }
     *     
     */
    public void setCertSubjectDNAttribute(CertSubjectDNAttribute value) {
        this.certSubjectDNAttribute = value;
    }

    /**
     * Obtient la valeur de la propriete extendedKeyUsage.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedKeyUsage }
     *     
     */
    public ExtendedKeyUsage getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    /**
     * Definit la valeur de la propriete extendedKeyUsage.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedKeyUsage }
     *     
     */
    public void setExtendedKeyUsage(ExtendedKeyUsage value) {
        this.extendedKeyUsage = value;
    }

}
