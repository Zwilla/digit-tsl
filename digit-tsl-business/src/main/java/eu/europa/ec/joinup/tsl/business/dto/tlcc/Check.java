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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Classe Java pour anonymous complex type.
 * <p>
 * Le fragment de schema suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="checkId" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "content"
})
@XmlRootElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class Check {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "checkId", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String checkId;
    @XmlAttribute(name = "status", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String status;

    /**
     * Obtient la valeur de la propriete content.
     *
     * @return
     *         possible object is
     *         {@link String }
     */
    public String getContent() {
        return content;
    }

    /**
     * Definit la valeur de la propriete content.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setContent(String value) {
        content = value;
    }

    /**
     * Obtient la valeur de la propriete checkId.
     *
     * @return
     *         possible object is
     *         {@link String }
     */
    public String getCheckId() {
        return checkId;
    }

    /**
     * Definit la valeur de la propriete checkId.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setCheckId(String value) {
        checkId = value;
    }

    /**
     * Obtient la valeur de la propriete status.
     *
     * @return
     *         possible object is
     *         {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Definit la valeur de la propriete status.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setStatus(String value) {
        status = value;
    }

    @Override
    public String toString() {
        return "[checkId=" + checkId + ", status=" + status + "content=" + content + "]";
    }

}
