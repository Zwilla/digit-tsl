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
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceTypeIdentifier" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceName" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceDigitalIdentity" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceStatus" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}StatusStartingTime" minOccurs="0"/>
 *         &lt;element ref="{http://www.etsi.org/19162/conformanceChecker}ServiceInformationExtensions" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "serviceTypeIdentifier", "serviceName", "serviceDigitalIdentity", "serviceStatus", "statusStartingTime",
		"serviceInformationExtensions", "check" })
@XmlRootElement(name = "ServiceHistoryInstance", namespace = "http://www.etsi.org/19162/conformanceChecker")
public class ServiceHistoryInstance {

	@XmlElement(name = "ServiceTypeIdentifier", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected ServiceTypeIdentifier serviceTypeIdentifier;
	@XmlElement(name = "ServiceName", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected ServiceName serviceName;
	@XmlElement(name = "ServiceDigitalIdentity", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected ServiceDigitalIdentity serviceDigitalIdentity;
	@XmlElement(name = "ServiceStatus", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected ServiceStatus serviceStatus;
	@XmlElement(name = "StatusStartingTime", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected StatusStartingTime statusStartingTime;
	@XmlElement(name = "ServiceInformationExtensions", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected ServiceInformationExtensions serviceInformationExtensions;
	@XmlElement(name = "Check", namespace = "http://www.etsi.org/19162/conformanceChecker")
	protected List<Check> check;
	@XmlAttribute(name = "index", required = true)
	protected BigInteger index;

	/**
	 * Obtient la valeur de la propriete serviceTypeIdentifier.
	 *
	 * @return
	 * 		possible object is
	 *         {@link ServiceTypeIdentifier }
	 *
	 */
	public ServiceTypeIdentifier getServiceTypeIdentifier() {
		return serviceTypeIdentifier;
	}

	/**
	 * Definit la valeur de la propriete serviceTypeIdentifier.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link ServiceTypeIdentifier }
	 *
	 */
	public void setServiceTypeIdentifier(ServiceTypeIdentifier value) {
		this.serviceTypeIdentifier = value;
	}

	/**
	 * Obtient la valeur de la propriete serviceName.
	 *
	 * @return
	 * 		possible object is
	 *         {@link ServiceName }
	 *
	 */
	public ServiceName getServiceName() {
		return serviceName;
	}

	/**
	 * Definit la valeur de la propriete serviceName.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link ServiceName }
	 *
	 */
	public void setServiceName(ServiceName value) {
		this.serviceName = value;
	}

	/**
	 * Obtient la valeur de la propriete serviceDigitalIdentity.
	 *
	 * @return
	 * 		possible object is
	 *         {@link ServiceDigitalIdentity }
	 *
	 */
	public ServiceDigitalIdentity getServiceDigitalIdentity() {
		return serviceDigitalIdentity;
	}

	/**
	 * Definit la valeur de la propriete serviceDigitalIdentity.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link ServiceDigitalIdentity }
	 *
	 */
	public void setServiceDigitalIdentity(ServiceDigitalIdentity value) {
		this.serviceDigitalIdentity = value;
	}

	/**
	 * Obtient la valeur de la propriete serviceStatus.
	 *
	 * @return
	 * 		possible object is
	 *         {@link ServiceStatus }
	 *
	 */
	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	/**
	 * Definit la valeur de la propriete serviceStatus.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link ServiceStatus }
	 *
	 */
	public void setServiceStatus(ServiceStatus value) {
		this.serviceStatus = value;
	}

	/**
	 * Obtient la valeur de la propriete statusStartingTime.
	 *
	 * @return
	 * 		possible object is
	 *         {@link StatusStartingTime }
	 *
	 */
	public StatusStartingTime getStatusStartingTime() {
		return statusStartingTime;
	}

	/**
	 * Definit la valeur de la propriete statusStartingTime.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link StatusStartingTime }
	 *
	 */
	public void setStatusStartingTime(StatusStartingTime value) {
		this.statusStartingTime = value;
	}

	/**
	 * Obtient la valeur de la propriete serviceInformationExtensions.
	 *
	 * @return
	 * 		possible object is
	 *         {@link ServiceInformationExtensions }
	 *
	 */
	public ServiceInformationExtensions getServiceInformationExtensions() {
		return serviceInformationExtensions;
	}

	/**
	 * Definit la valeur de la propriete serviceInformationExtensions.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link ServiceInformationExtensions }
	 *
	 */
	public void setServiceInformationExtensions(ServiceInformationExtensions value) {
		this.serviceInformationExtensions = value;
	}

	/**
	 * Obtient la valeur de la propriete index.
	 *
	 * @return
	 * 		possible object is
	 *         {@link BigInteger }
	 *
	 */
	public BigInteger getIndex() {
		return index;
	}

	/**
	 * Definit la valeur de la propriete index.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link BigInteger }
	 *
	 */
	public void setIndex(BigInteger value) {
		this.index = value;
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
