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

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;

public class ContactJaxbTest {

    @Test
    public void testMarshal() throws Exception {

        File file = new File("src/test/resources/contact-marshal.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ContactList.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ContactList contactList = (ContactList) jaxbUnmarshaller.unmarshal(file);
        for (TLSOContact tlso : contactList.getTLSOContact()) {
            Assert.assertNotNull(tlso);
            System.out.println(tlso);
        }
        Assert.assertEquals(32, contactList.getTLSOContact().size());
    }
}
