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
package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLElectronicAddress;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ContactService {

    private static Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    @Value("${contact.url}")
    private String contactUrl;

    @Value("${use.tl.contact:false}")
    private Boolean useTLContact;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    public List<TLSOContact> getContact() {
        File file = new File(contactUrl);
        try {
            JAXBContext jc = JAXBContext.newInstance(ContactList.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLInputFactory xif = XMLInputFactory.newInstance();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(file));
            ContactList contactList = (ContactList) unmarshaller.unmarshal(xsr);
            return contactList.getTLSOContact();
        } catch (JAXBException | XMLStreamException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    /** Get contact.xml **/
    public TLSOContact getContactByTerritory(String territory) {
        List<TLSOContact> tmpList = getContact();
        if (tmpList != null) {
            for (TLSOContact contact : tmpList) {
                if (contact.getTerritory().equalsIgnoreCase(territory)) {
                    return contact;
                }
            }
        }
        return new TLSOContact();
    }

    public List<TLSOContact> getAllContact() {
        List<TLSOContact> contactList = new ArrayList<>();
        for (String country : countryService.getAllCountryCode()) {
            contactList.add(getAllContactByTerritory(country));
        }
        return contactList;
    }

    /**
     * Get contact XML and trusted list (if property useTLContact set true) by territory
     *
     * @param territory
     */
    public TLSOContact getAllContactByTerritory(String territory) {
        TLSOContact resultContact = new TLSOContact();
        TLSOContact contact = getContactByTerritory(territory);
        if (useTLContact) {
            TL tl = tlService.getPublishedTLByCountryCode(territory);
            if ((tl != null) && (tl.getSchemeInformation() != null) && (tl.getSchemeInformation().getSchemeOpeElectronic() != null)) {
                for (TLElectronicAddress tlElectronic : tl.getSchemeInformation().getSchemeOpeElectronic()) {
                    if (tlElectronic.getValue().startsWith("mailto:")) {
                        String tlAddress = tlElectronic.getValue();
                        tlAddress = tlAddress.replace("mailto:", "");
                        Boolean isPresent = false;
                        for (String contactElectronic : contact.getElectronicAddress().getURI()) {
                            if (tlAddress.trim().equalsIgnoreCase(contactElectronic.trim())) {
                                isPresent = true;
                            }
                        }
                        if (!isPresent) {
                            contact.getElectronicAddress().getURI().add(tlAddress.trim());
                        }
                    }
                }
            }
        }
        resultContact = contact;
        return resultContact;
    }

    public List<TLDifference> getContactChanges(TLSOContact editContact, String territory, String id) {
        List<TLDifference> listDiff = new ArrayList<>();

        // get edited contact
        List<String> editElectronicAddress = new ArrayList<>();
        if ((editContact != null) && (editContact.getElectronicAddress() != null) && (editContact.getElectronicAddress().getURI() != null)) {
            editElectronicAddress = editContact.getElectronicAddress().getURI();
        }

        // get published contact
        TLSOContact publishedContact = getContactByTerritory(territory);
        List<String> publishedElectronicAddress = new ArrayList<>();
        if ((publishedContact != null) && (publishedContact.getElectronicAddress() != null) && (publishedContact.getElectronicAddress().getURI() != null)) {
            publishedElectronicAddress = publishedContact.getElectronicAddress().getURI();
        }

        // Get published Delete
        List<String> tmpEditElectronicAddress = new ArrayList<>(editElectronicAddress);
        for (String pAddress : publishedElectronicAddress) {
            if (editElectronicAddress.contains(pAddress)) {
                tmpEditElectronicAddress.remove(pAddress);
            }
        }
        TLDifference diff = new TLDifference();
        for (String addedAddress : tmpEditElectronicAddress) {
            if (!StringUtils.isEmpty(addedAddress)) {
                diff = new TLDifference(id + "_" + Tag.CONTACT + "_" + Tag.ELECTRONIC_ADDRESS, "", addedAddress);
                listDiff.add(diff);
            }
        }

        // Get published Added
        List<String> tmpPublishedElectronic = new ArrayList<>(publishedElectronicAddress);
        for (String eAddress : editElectronicAddress) {
            if (publishedElectronicAddress.contains(eAddress)) {
                tmpPublishedElectronic.remove(eAddress);
            }
        }
        for (String deleteAddress : tmpPublishedElectronic) {
            diff = new TLDifference(id + "_" + Tag.CONTACT + "_" + Tag.ELECTRONIC_ADDRESS, deleteAddress, "");
            listDiff.add(diff);
        }

        // Name
        if ((publishedContact.getName() != null) && !publishedContact.getName().equals(editContact.getName())) {
            if (StringUtils.isEmpty(editContact.getName())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_NAME, publishedContact.getName(), "");
            } else if (StringUtils.isEmpty(publishedContact.getName())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_NAME, "", editContact.getName());
            } else {
                diff = new TLDifference(id + "_" + Tag.CONTACT_NAME, publishedContact.getName(), editContact.getName());
            }
            diff.setHrLocation("Name");
            listDiff.add(diff);
        } else if ((publishedContact.getName() == null) && (editContact.getName() != null)) {
            diff = new TLDifference(id + "_" + Tag.CONTACT_NAME.toString(), "", editContact.getName());
        }

        // Postal Address
        if ((publishedContact.getPostalAddress() != null) && !publishedContact.getPostalAddress().equals(editContact.getPostalAddress())) {
            if (StringUtils.isEmpty(editContact.getPostalAddress())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_ADDRESS.toString(), publishedContact.getPostalAddress(), "");
            } else if (StringUtils.isEmpty(publishedContact.getPostalAddress())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_ADDRESS.toString(), "", editContact.getPostalAddress());
            } else {
                diff = new TLDifference(id + "_" + Tag.CONTACT_ADDRESS.toString(), publishedContact.getPostalAddress(), editContact.getPostalAddress());
            }
            diff.setHrLocation("Address");
            listDiff.add(diff);
        } else if ((publishedContact.getPostalAddress() == null) && (editContact.getPostalAddress() != null)) {
            diff = new TLDifference(id + "_" + Tag.CONTACT_ADDRESS.toString(), "", editContact.getPostalAddress());
        }

        // Phone number
        if ((publishedContact.getPhoneNumber() != null) && !publishedContact.getPhoneNumber().equals(editContact.getPhoneNumber())) {
            if (StringUtils.isEmpty(editContact.getPhoneNumber())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_PHONE.toString(), publishedContact.getPhoneNumber(), "");
            } else if (StringUtils.isEmpty(publishedContact.getPhoneNumber())) {
                diff = new TLDifference(id + "_" + Tag.CONTACT_PHONE.toString(), "", editContact.getPhoneNumber());
            } else {
                diff = new TLDifference(id + "_" + Tag.CONTACT_PHONE.toString(), publishedContact.getPhoneNumber(), editContact.getPhoneNumber());
            }
            diff.setHrLocation("Phone");
            listDiff.add(diff);
        } else if ((publishedContact.getPhoneNumber() == null) && (editContact.getPhoneNumber() != null)) {
            diff = new TLDifference(id + "_" + Tag.CONTACT_PHONE.toString(), "", editContact.getPhoneNumber());
        }

        return listDiff;
    }

    public void editContactFile(TLSOContact tlsoContact) {
        tlsoContact.setSubmissionDate(TLUtils.toXMLGregorianDate(new Date()));
        List<TLSOContact> contactList = getContact();
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getTerritory().equalsIgnoreCase(tlsoContact.getTerritory())) {
                contactList.set(i, tlsoContact);
            }
        }
        ContactList jaxbContact = new ContactList();
        jaxbContact.getTLSOContact().addAll(contactList);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ContactList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(jaxbContact, new StreamResult(out));

            FileOutputStream fos;
            fos = new FileOutputStream(new File(contactUrl));
            out.writeTo(fos);
            fos.close();
        } catch (Exception e) {
            LOGGER.error("Contact xml file edition error." + e.getMessage(), e);
        }

    }

    public List<String> getAllEmailToContactByTerritory(TLSOContact tlsoContact) {
        TLSOContact tmpContact = getContactByTerritory(tlsoContact.getTerritory());
        List<String> emailContact = new ArrayList<>(tlsoContact.getElectronicAddress().getURI());
        for (String tmpEmail : tmpContact.getElectronicAddress().getURI()) {
            if (!emailContact.contains(tmpEmail)) {
                emailContact.add(tmpEmail);
            }
        }
        return emailContact;
    }

    public List<String> getDeletedContact(TLSOContact contact) {
        List<String> deletedContact = new ArrayList<>();
        TLSOContact tmpContact = getContactByTerritory(contact.getTerritory());
        for (String tmpEmail : tmpContact.getElectronicAddress().getURI()) {
            if (!contact.getElectronicAddress().getURI().contains(tmpEmail)) {
                deletedContact.add(tmpEmail);
            }
        }
        return deletedContact;
    }
}
