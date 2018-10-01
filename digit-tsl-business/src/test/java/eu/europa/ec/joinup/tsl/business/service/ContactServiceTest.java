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

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact.ElectronicAddress;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class ContactServiceTest extends AbstractSpringTest {


    @Value("${contact.url}")
    private String contactUrl;

    private static final String BE_COUNTRY_CODE = "BE";
    private static final String BE_XML_URL = "http://tsl.belgium.be/tsl-be.xml";

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private ContactService contactService;

    @Before
    public void initialize() {
        Load load = new Load();
        tlLoader.loadTL(BE_COUNTRY_CODE, BE_XML_URL, TLType.TL, TLStatus.PROD, load);
        TLSOContact contact = new TLSOContact();
        contact.setTerritory(BE_COUNTRY_CODE);
        contact.setName("name");
        contact.setPhoneNumber("phone number");
        contact.setPostalAddress("postal address");
        contact.setElectronicAddress(new ElectronicAddress());
        contact.getElectronicAddress().getURI().add("email@test");
        contactService.editContactFile(contact);
    }

    @Test
    public void getContactByTerritory() {
        TLSOContact contact = contactService.getAllContactByTerritory(BE_COUNTRY_CODE);
        Assert.assertNotNull(contact);
        Assert.assertEquals("BE", contact.getTerritory());
        Assert.assertEquals("name", contact.getName());
        Assert.assertEquals("phone number", contact.getPhoneNumber());
        Assert.assertEquals("postal address", contact.getPostalAddress());
        Assert.assertEquals(1, contact.getElectronicAddress().getURI().size());
    }

    @Test
    public void BgetAllContactByTerritory() {
        TLSOContact contact = contactService.getAllContactByTerritory(BE_COUNTRY_CODE);
        Assert.assertNotNull(contact);
        Assert.assertEquals("BE", contact.getTerritory());
        Assert.assertEquals("name", contact.getName());
        Assert.assertEquals("phone number", contact.getPhoneNumber());
        Assert.assertEquals("postal address", contact.getPostalAddress());
        Assert.assertEquals(1, contact.getElectronicAddress().getURI().size());
        Assert.assertEquals("email@test", contact.getElectronicAddress().getURI().get(0));
    }

    @Test
    public void CgetContact() {
        List<TLSOContact> contact = contactService.getContact();
        Assert.assertNotNull(contact);
        Assert.assertEquals(2, contact.size());
    }

    @Test
    public void getContactChanges(){
        TLSOContact contact = contactService.getAllContactByTerritory(BE_COUNTRY_CODE);
        contact.getElectronicAddress().getURI().add("new");
        List<TLDifference> diff = contactService.getContactChanges(contact, BE_COUNTRY_CODE, "");
        Assert.assertNotNull(diff);
        Assert.assertEquals("new", diff.get(0).getCurrentValue());
        Assert.assertEquals("", diff.get(0).getPublishedValue());
    }

    @Test
    public void EeditContactXml(){
        TLSOContact contact = new TLSOContact();
        contact.setTerritory(BE_COUNTRY_CODE);
        contact.setName("edit name");
        contact.setPhoneNumber("edit phone number");
        contact.setPostalAddress("edit postal address");
        contact.setElectronicAddress(new ElectronicAddress());
        contactService.editContactFile(contact);
        TLSOContact contactEdited = contactService.getAllContactByTerritory(BE_COUNTRY_CODE);
        Assert.assertNotNull(contactEdited);
        Assert.assertEquals("BE", contactEdited.getTerritory());
        Assert.assertEquals("edit name", contactEdited.getName());
        Assert.assertEquals("edit phone number", contactEdited.getPhoneNumber());
        Assert.assertEquals("edit postal address", contactEdited.getPostalAddress());

    }
}
