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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}DocumentationReferences" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "identifier", "documentationReferences" })
@XmlRootElement(name = "PolicyIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class PolicyIdentifier {

    @XmlElement(name = "Identifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<Identifier> identifier;
    @XmlElement(name = "DocumentationReferences", namespace = "http://www.etsi.org/19162/conformanceChecker")
    protected List<DocumentationReferences> documentationReferences;
    @XmlAttribute(name = "index", required = true)
    protected BigInteger index;

    public List<Identifier> getIdentifier() {
        if (this.identifier == null) {
            this.identifier = new ArrayList<Identifier>();
        }
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public void setDocumentationReferences(List<DocumentationReferences> documentationReferences) {
        this.documentationReferences = documentationReferences;
    }

    public List<DocumentationReferences> getDocumentationReferences() {
        if (documentationReferences == null) {
            documentationReferences = new ArrayList<DocumentationReferences>();
        }
        return this.documentationReferences;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger value) {
        this.index = value;
    }

}
