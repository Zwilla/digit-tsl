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

package eu.europa.esig.jaxb.v5.tsl;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for AddressType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PostalAddresses"/&gt;
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ElectronicAddress"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = { "postalAddresses", "electronicAddress" })
public class AddressTypeV5 implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "PostalAddresses", required = true)
    protected PostalAddressListTypeV5 postalAddresses;
    @XmlElement(name = "ElectronicAddress", required = true)
    protected ElectronicAddressTypeV5 electronicAddress;

    /**
     * Gets the value of the postalAddresses property.
     * 
     * @return possible object is {@link PostalAddressListTypeV5 }
     * 
     */
    public PostalAddressListTypeV5 getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Sets the value of the postalAddresses property.
     * 
     * @param value
     *            allowed object is {@link PostalAddressListTypeV5 }
     * 
     */
    public void setPostalAddresses(PostalAddressListTypeV5 value) {
        this.postalAddresses = value;
    }

    /**
     * Gets the value of the electronicAddress property.
     * 
     * @return possible object is {@link ElectronicAddressTypeV5 }
     * 
     */
    public ElectronicAddressTypeV5 getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * Sets the value of the electronicAddress property.
     * 
     * @param value
     *            allowed object is {@link ElectronicAddressTypeV5 }
     * 
     */
    public void setElectronicAddress(ElectronicAddressTypeV5 value) {
        this.electronicAddress = value;
    }

}
