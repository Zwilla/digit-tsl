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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
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
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}CriteriaList" minOccurs="0"/>
 *           &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}KeyUsage" minOccurs="0"/>
 *           &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}PolicySet" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}otherCriteriaList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "criteriaListOrKeyUsageOrPolicySet", "otherCriteriaList","check" })
@XmlRootElement(name = "CriteriaList", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class CriteriaList {

	@XmlElements({ @XmlElement(name = "CriteriaList", namespace = "http://www.etsi.org/19162/conformanceChecker", type = CriteriaList.class),
		@XmlElement(name = "KeyUsage", namespace = "http://www.etsi.org/19162/conformanceChecker", type = KeyUsage.class),
		@XmlElement(name = "PolicySet", namespace = "http://www.etsi.org/19162/conformanceChecker", type = PolicySet.class) })
	protected List<Object> criteriaListOrKeyUsageOrPolicySet;
	@XmlElement(namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected OtherCriteriaList otherCriteriaList;
	@XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected List<Check> check;

	public List<Object> getCriteriaListOrKeyUsageOrPolicySet() {
		if (criteriaListOrKeyUsageOrPolicySet == null) {
			criteriaListOrKeyUsageOrPolicySet = new ArrayList<Object>();
		}
		return this.criteriaListOrKeyUsageOrPolicySet;
	}

	public OtherCriteriaList getOtherCriteriaList() {
		return otherCriteriaList;
	}

	public void setOtherCriteriaList(OtherCriteriaList value) {
		this.otherCriteriaList = value;
	}

	public List<Check> getCheck() {
		if (this.check == null) {
			this.check = new ArrayList<Check>();
		}
		return check;
	}

	public void setCheck(List<Check> check) {
		this.check = check;
	}

}
