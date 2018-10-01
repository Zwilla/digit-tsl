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
package eu.europa.ec.joinup.tsl.business.dto.contact;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java pour anonymous complex type.
 *
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TLSOContact" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PostalAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="SubmissionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element name="Territory" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ElectronicAddress">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="URI" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
		"tlsoContact"
})
@XmlRootElement(name = "ContactList")
public class ContactList {

	@XmlElement(name = "TLSOContact")
	protected List<ContactList.TLSOContact> tlsoContact;

	/**
	 * Gets the value of the tlsoContact property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the tlsoContact property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * <pre>
	 *    getTLSOContact().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ContactList.TLSOContact }
	 *
	 *
	 */
	public List<ContactList.TLSOContact> getTLSOContact() {
		if (tlsoContact == null) {
			tlsoContact = new ArrayList<ContactList.TLSOContact>();
		}
		return this.tlsoContact;
	}


	/**
	 * <p>Classe Java pour anonymous complex type.
	 *
	 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
	 *
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="PostalAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="SubmissionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
	 *         &lt;element name="Territory" type="{http://www.w3.org/2001/XMLSchema}string"/>
	 *         &lt;element name="ElectronicAddress">
	 *           &lt;complexType>
	 *             &lt;complexContent>
	 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *                 &lt;sequence>
	 *                   &lt;element name="URI" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
	 *                 &lt;/sequence>
	 *               &lt;/restriction>
	 *             &lt;/complexContent>
	 *           &lt;/complexType>
	 *         &lt;/element>
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
			"name",
			"postalAddress",
			"phoneNumber",
			"submissionDate",
			"territory",
			"electronicAddress"
	})
	public static class TLSOContact {

		@XmlElement(name = "Name", required = true)
		protected String name;
		@XmlElement(name = "PostalAddress", required = true)
		protected String postalAddress;
		@XmlElement(name = "PhoneNumber", required = true)
		protected String phoneNumber;
		@XmlElement(name = "SubmissionDate")
		@XmlSchemaType(name = "dateTime")
		protected XMLGregorianCalendar submissionDate;
		@XmlElement(name = "Territory", required = true)
		protected String territory;
		@XmlElement(name = "ElectronicAddress", required = true)
		protected ContactList.TLSOContact.ElectronicAddress electronicAddress;

		public TLSOContact() {
			super();
			this.electronicAddress = new ElectronicAddress();
			this.electronicAddress.uri = new ArrayList<String>();
		}

		/**
		 * Obtient la valeur de la propri�t� name.
		 *
		 * @return
		 *     possible object is
		 *     {@link String }
		 *
		 */
		public String getName() {
			return name;
		}

		/**
		 * D�finit la valeur de la propri�t� name.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link String }
		 *
		 */
		public void setName(String value) {
			this.name = value;
		}

		/**
		 * Obtient la valeur de la propri�t� postalAddress.
		 *
		 * @return
		 *     possible object is
		 *     {@link String }
		 *
		 */
		public String getPostalAddress() {
			return postalAddress;
		}

		/**
		 * D�finit la valeur de la propri�t� postalAddress.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link String }
		 *
		 */
		public void setPostalAddress(String value) {
			this.postalAddress = value;
		}

		/**
		 * Obtient la valeur de la propri�t� phoneNumber.
		 *
		 * @return
		 *     possible object is
		 *     {@link String }
		 *
		 */
		public String getPhoneNumber() {
			return phoneNumber;
		}

		/**
		 * D�finit la valeur de la propri�t� phoneNumber.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link String }
		 *
		 */
		public void setPhoneNumber(String value) {
			this.phoneNumber = value;
		}

		/**
		 * Obtient la valeur de la propri�t� submissionDate.
		 *
		 * @return
		 *     possible object is
		 *     {@link XMLGregorianCalendar }
		 *
		 */
		public XMLGregorianCalendar getSubmissionDate() {
			return submissionDate;
		}

		/**
		 * D�finit la valeur de la propri�t� submissionDate.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link XMLGregorianCalendar }
		 *
		 */
		public void setSubmissionDate(XMLGregorianCalendar value) {
			this.submissionDate = value;
		}

		/**
		 * Obtient la valeur de la propri�t� territory.
		 *
		 * @return
		 *     possible object is
		 *     {@link String }
		 *
		 */
		public String getTerritory() {
			return territory;
		}

		/**
		 * D�finit la valeur de la propri�t� territory.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link String }
		 *
		 */
		public void setTerritory(String value) {
			this.territory = value;
		}

		/**
		 * Obtient la valeur de la propri�t� electronicAddress.
		 *
		 * @return
		 *     possible object is
		 *     {@link ContactList.TLSOContact.ElectronicAddress }
		 *
		 */
		public ContactList.TLSOContact.ElectronicAddress getElectronicAddress() {
			return electronicAddress;
		}

		/**
		 * D�finit la valeur de la propri�t� electronicAddress.
		 *
		 * @param value
		 *     allowed object is
		 *     {@link ContactList.TLSOContact.ElectronicAddress }
		 *
		 */
		public void setElectronicAddress(ContactList.TLSOContact.ElectronicAddress value) {
			this.electronicAddress = value;
		}


		/**
		 * <p>Classe Java pour anonymous complex type.
		 *
		 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
		 *
		 * <pre>
		 * &lt;complexType>
		 *   &lt;complexContent>
		 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
		 *       &lt;sequence>
		 *         &lt;element name="URI" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
				"uri"
		})
		public static class ElectronicAddress {

			@XmlElement(name = "URI")
			protected List<String> uri;

			/**
			 * Gets the value of the uri property.
			 *
			 * <p>
			 * This accessor method returns a reference to the live list,
			 * not a snapshot. Therefore any modification you make to the
			 * returned list will be present inside the JAXB object.
			 * This is why there is not a <CODE>set</CODE> method for the uri property.
			 *
			 * <p>
			 * For example, to add a new item, do as follows:
			 * <pre>
			 *    getURI().add(newItem);
			 * </pre>
			 *
			 *
			 * <p>
			 * Objects of the following type(s) are allowed in the list
			 * {@link String }
			 *
			 *
			 */
			public List<String> getURI() {
				if (uri == null) {
					uri = new ArrayList<String>();
				}
				return this.uri;
			}

		}

	}

}
