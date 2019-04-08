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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPInformation" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}TSPServices" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "tspInformation", "tspServices" })
@XmlRootElement(name = "TrustServiceProvider", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class TrustServiceProvider {

    @XmlElement(name = "TSPInformation", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPInformation tspInformation;
    @XmlElement(name = "TSPServices", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected TSPServices tspServices;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    /**
     * Obtient la valeur de la propriete tspInformation.
     * 
     * @return possible object is {@link TSPInformation }
     * 
     */
    public TSPInformation getTSPInformation() {
        return tspInformation;
    }

    /**
     * Definit la valeur de la propriete tspInformation.
     * 
     * @param value
     *            allowed object is {@link TSPInformation }
     * 
     */
    public void setTSPInformation(TSPInformation value) {
        this.tspInformation = value;
    }

    /**
     * Obtient la valeur de la propriete tspServices.
     * 
     * @return possible object is {@link TSPServices }
     * 
     */
    public TSPServices getTSPServices() {
        return tspServices;
    }

    /**
     * Definit la valeur de la propriete tspServices.
     * 
     * @param value
     *            allowed object is {@link TSPServices }
     * 
     */
    public void setTSPServices(TSPServices value) {
        this.tspServices = value;
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

}
