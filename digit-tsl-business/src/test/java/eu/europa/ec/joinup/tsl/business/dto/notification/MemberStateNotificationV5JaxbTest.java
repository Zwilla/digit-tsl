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
package eu.europa.ec.joinup.tsl.business.dto.notification;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact.ElectronicAddress;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.service.TrustedListJaxbService;
import eu.europa.ec.joinup.tsl.business.service.NotificationJaxbService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.TLNotificationMapper;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;
import eu.europa.esig.jaxb.v5.tsl.OtherTSLPointerTypeV5;

public class MemberStateNotificationV5JaxbTest extends AbstractSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberStateNotificationV5JaxbTest.class);

    private static final String notifPath = "src/test/resources/notificationMarshall.xml";
    private static final String notifPathSrc = "src/test/resources/notification.xml";

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private NotificationJaxbService notificationJaxbService;

    @Autowired
    private UserService userService;

    // Generated Jaxb classes

    @Test
    public void testUnMarshal() throws Exception {
        File file = new File(notifPathSrc);
        JAXBContext jaxbContext = JAXBContext.newInstance(MemberStateNotificationV5.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        MemberStateNotificationV5 notification = (MemberStateNotificationV5) jaxbUnmarshaller.unmarshal(file);
        Assert.assertNotNull(notification);
    }

    @Test
    public void testMarshall() throws Exception {

        File testFile = new File(notifPath);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Contact
        TLSOContact contact = new TLSOContact();
        contact.setName("Name");
        contact.setPhoneNumber("+35212345678");
        contact.setPostalAddress("postal address");
        ElectronicAddress elec = new ElectronicAddress();
        elec.getURI().add("email@test.fr");
        contact.setElectronicAddress(elec);

        // Pointer
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));

        NotificationPointers notification = new NotificationPointers();
        if (unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer() != null) {
            notification.setMpPointer(new TLPointersToOtherTSL(0, "test", unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer().get(0)));
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(MemberStateNotificationV5.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        Date d = new Date();
        List<String> contactDeleted = new ArrayList<String>();
        contactDeleted.add("test1");
        contactDeleted.add("test2");

        notification.setDateOfSubmission(d);
        notification.setDateOfEffect(d);
        notification.setUsers(userService.getUsersOrderByCountryAndName());
        TLSOContact tlsoContact = new TLSOContact();
        tlsoContact.setName("name");
        tlsoContact.setPhoneNumber("060121151");
        tlsoContact.setPostalAddress("postalAddress");
        tlsoContact.setTerritory("BE");
        tlsoContact.setElectronicAddress(new ElectronicAddress());
        notification.setTlsoContact(tlsoContact);

        MemberStateNotificationV5 msNotification = TLNotificationMapper.mapDTONotificationToXMLNotificationObj(notification, "notification identifier", contactDeleted);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        jaxbMarshaller.marshal(msNotification, new StreamResult(out));

        FileOutputStream fos = new FileOutputStream(new File(notifPath));
        out.writeTo(fos);
        fos.close();

        Assert.assertNotNull(new File(notifPath));
    }

    // Service
    @Test
    public void unmarshallService() {
        MemberStateNotificationV5 notification = null;
        try {
            InputStream is = new FileInputStream(notifPathSrc);
            notification = notificationJaxbService.unmarshallNotification(is);
        } catch (Exception e) {
            LOGGER.error("Unmarshall Notifcation." + e);
        }
        Assert.assertNotNull(notification);
    }

    @Test
    public void marshalService() throws Exception {

        File testFile = new File(notifPath);
        if (testFile.exists()) {
            testFile.delete();
        }

        // Contact
        TLSOContact contact = new TLSOContact();
        contact.setName("toto");
        contact.setPhoneNumber("+555555");
        contact.setPostalAddress("postal address");
        ElectronicAddress elec = new ElectronicAddress();
        elec.getURI().add("email@test.fr");
        contact.setElectronicAddress(elec);

        // Pointer
        TrustStatusListType unmarshall;
        List<OtherTSLPointerTypeV5> tlPointers = new ArrayList<OtherTSLPointerTypeV5>();
        unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));

        if (unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer() != null) {
            tlPointers.add(new TLPointersToOtherTSL(0, "test", unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer().get(0)).asTSLTypeV5());
        }

        MemberStateNotificationV5 notif = new MemberStateNotificationV5();
        notif.setSchemeContact(contact);
        notif.setEffectDate(TLUtils.toXMLGregorianDate(new Date()));
        notif.setNotificationIdentifier("notif identifier");
        notif.setPointersToOtherTSL(tlPointers);
        notif.setSubmissionDate(TLUtils.toXMLGregorianDate(new Date()));
        notif.setToolIdentifier("tool identifier");
        notif.setVersionIdentifier(1);
        notif.setUsers(userService.getUsersOrderByCountryAndName());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        notificationJaxbService.marshallNotification(notif, out);

        FileOutputStream fos = new FileOutputStream(new File(notifPathSrc));
        out.writeTo(fos);
        fos.close();

        Assert.assertNotNull(new File(notifPath));
    }

    // @Test
    // public void testMimetype() throws Exception {
    //
    // File testFile = new File(notifPath);
    // if (testFile.exists()) {
    // testFile.delete();
    // }
    //
    // // Contact
    // TLSOContact contact = new TLSOContact();
    // contact.setName("toto");
    // contact.setPhoneNumber("+555555");
    // contact.setPostalAddress("postal address");
    // ElectronicAddress elec = new ElectronicAddress();
    // elec.getURI().add("email@test.fr");
    // contact.setElectronicAddress(elec);
    //
    // // Pointer
    // TrustStatusListType unmarshall;
    // List<OtherTSLPointerTypeV5> tlPointers = new ArrayList<OtherTSLPointerTypeV5>();
    // unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));
    //
    // if (unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer() != null) {
    // tlPointers.add(new TLPointersToOtherTSL(0, "test", unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer().get(0))
    // .asTSLTypeV5());
    // }
    //
    // MemberStateNotificationV5 notif = new MemberStateNotificationV5();
    // notif.setEffectDate(TLUtils.toXMLGregorianDate(new Date()));
    // notif.setNotificationIdentifier("notif identifier");
    // notif.setSubmissionDate(TLUtils.toXMLGregorianDate(new Date()));
    // notif.setToolIdentifier("tool identifier");
    // notif.setPointersToOtherTSL(tlPointers);
    // notif.setVersionIdentifier(1);
    //
    // Assert.assertNotNull(notif.getPointersToOtherTSL());
    // Assert.assertEquals(1, notif.getPointersToOtherTSL().size());
    //
    // List<Serializable> textualInformationOrOtherInformation = notif.getPointersToOtherTSL().get(0).getAdditionalInformation()
    // .getTextualInformationOrOtherInformation();
    // Map<String, Object> properties = TLUtils.extractAsMapV5(textualInformationOrOtherInformation);
    //
    // Object mm = properties.get("{http://uri.etsi.org/02231/v2/additionaltypes#}MimeType");
    // if ((mm instanceof String) && (((String) mm).length() > 1)) {
    // MimeType mimetype = TLUtils.convert((String) mm);
    // Assert.assertEquals(MimeType.XML, mimetype);
    // } else {
    // Assert.fail();
    // }
    //
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // notificationJaxbService.marshallNotification(notif, out);
    //
    // MemberStateNotificationV5 notif2 = notificationJaxbService.unmarshallNotification(new ByteArrayInputStream(out.toByteArray()));
    //
    // Assert.assertNotNull(notif2.getPointersToOtherTSL());
    // Assert.assertEquals(1, notif2.getPointersToOtherTSL().size());
    //
    // textualInformationOrOtherInformation = notif2.getPointersToOtherTSL().get(0).getAdditionalInformation()
    // .getTextualInformationOrOtherInformation();
    // properties = TLUtils.extractAsMapV5(textualInformationOrOtherInformation);
    //
    // mm = properties.get("{http://uri.etsi.org/02231/v2/additionaltypes#}MimeType");
    // if ((mm instanceof String) && (((String) mm).length() > 1)) {
    // MimeType mimetype = TLUtils.convert((String) mm);
    // Assert.assertEquals(MimeType.XML, mimetype);
    // } else {
    // Assert.fail();
    // }
    //
    // }

}
