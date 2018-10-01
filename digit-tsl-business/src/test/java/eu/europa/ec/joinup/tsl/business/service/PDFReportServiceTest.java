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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class PDFReportServiceTest extends AbstractSpringTest {

    @Autowired
    private PDFReportService reportService;

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private RulesRunnerService rulesRunnerService;

    @Autowired
    private TLService tlService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TLLoader tlLoader;

    @Before
    public void initialize() {
        Load load = new Load();
        tlLoader.loadTL("EU", getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);
        tlLoader.loadTL("BE", "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);
        tlLoader.loadTL("CZ", "https://tsl.gov.cz/publ/TSL_CZ.xtsl", TLType.TL, TLStatus.PROD, load);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testNotificationChangeReport() throws Exception {
        File file = new File("src/test/resources/notificationChange.pdf");
        if (file.exists()) {
            file.delete();
        }

        TrustStatusListType tsl = tlService.getLOTLProductionJaxb();
        TL tl = tlBuilder.buildTLV4(1, tsl);

        TLPointersToOtherTSL pointerHr = tl.getPointers().get(1);
        TLPointersToOtherTSL pointer = tl.getPointers().get(2);
        pointer.setTlLocation(pointer.getTlLocation() + "-testLocation");
        pointerHr.setTlLocation("Location test");
        pointer.getSchemeOpeName().get(0).setValue("Test scheme operation name");
        NotificationPointers notification = new NotificationPointers();
        notification.setCreationDate(new Date());
        notification.setDateOfEffect(new Date());
        notification.setDateOfSubmission(new Date());
        notification.setMpPointer(pointer);
        notification.setHrPointer(pointerHr);
        notification.setAddedCertificates(new ArrayList<String>());
        notification.setDeletedCertificates(new ArrayList<String>());
        TLSignature signature = new TLSignature();
        signature.setIndication(SignatureStatus.NOT_SIGNED);
        signature.setSignedBy("BOB");
        signature.setSigningDate(new Date());
        notification.setSignatureInformation(signature);

        TLSOContact contact = new TLSOContact();
        contact.setName("test");
        contact.setPhoneNumber("0669696969");
        contact.setPostalAddress("postal");
        contact.setTerritory("BE");

        notification.setTlsoContact(contact);

        List<DBUser> users = userService.findAuthenticatedUserByTerritory("LU");
        for (DBUser user : users) {
            notification.getUsers().add(new User(user));
        }

        TLDigitalIdentification digitalId = pointer.getServiceDigitalId().get(0);
        pointer.getServiceDigitalId().remove(0);
        List<TLDigitalIdentification> list = new ArrayList<>();
        list.add(digitalId);
        NotificationPointers originalPointer = notificationService.getNotification(notification.getMpPointer().getSchemeTerritory());
        TLCertificate cert = originalPointer.getMpPointer().getServiceDigitalId().get(0).getCertificateList().get(0);
        cert.setId("Test id");
        notification.getMpPointer().getServiceDigitalId().add(originalPointer.getMpPointer().getServiceDigitalId().get(0));
        List<String> listCert = new ArrayList<>();
        listCert.add("Cert1 : " + cert.getId());
        notification.setCurrentCertificates(listCert);

        FileOutputStream fos = new FileOutputStream(file);
        reportService.generateNotificationReport(notification, fos);
        Assert.assertTrue(file.exists());
    }

    @Test
    @DirtiesContext
    public void testCheckResultOrdered() throws Exception {
        File file = new File("src/test/resources/orderedCheckTest.pdf");
        if (file.exists()) {
            file.delete();
        }

        int id = createTLinDB(TLType.TL);
        TrustStatusListType tsl = jaxbService.unmarshallTSL(new File("src/test/resources/tsl/BE/2016-10-13_12-55-38.xml"));
        TL tl = tlBuilder.buildTLV4(id, tsl);
        tl.setDbCountryName("Belgium");
        tl.setDbName("DRAFT_BE");
        tl.setDbStatus(TLStatus.DRAFT);
        tl.setCheckEdited(new Date());
        tl.setTlId(id);

        DBTrustedLists dbTL = new DBTrustedLists();
        dbTL.setId(id);
        dbTL.setSequenceNumber(10000);
        dbTL.setName("db-tl");
        DBFiles xmlFile = new DBFiles();
        xmlFile.setMimeTypeFile(MimeType.XML);
        xmlFile.setLocalPath("src/test/resources/tsl/BE/2016-10-13_12-55-38.xml");
        xmlFile.setId(1000);
        dbTL.setXmlFile(xmlFile);
        dbTL.setType(TLType.TL);
        tlRepository.save(dbTL);
        rulesRunnerService.runAllRules(tl, null);

        FileOutputStream fos = new FileOutputStream(file);
        reportService.generateTLReport(tl, fos);

        Assert.assertTrue(file.exists());
    }

    private int createTLinDB(TLType type) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(type);
        trustedList.setXmlFile(new DBFiles());
        tlRepository.save(trustedList);
        return trustedList.getId();
    }

    @Test
    @DirtiesContext
    public void testCheckResultOrderedCZ() throws Exception {
        File file = new File("target/orderedCheckTestCZ.pdf");
        if (file.exists()) {
            file.delete();
        }

        int lotlId = createTLinDB(TLType.TL);
        TrustStatusListType tsl = jaxbService.unmarshallTSL(new File("src/test/resources/tsl/CZ/2016-10-13_13-09-15.xml"));
        TL tl = tlBuilder.buildTLV4(lotlId, tsl);
        tl.setDbCountryName("Czech Republic");
        tl.setDbName("PROD - CZ test");
        tl.setDbStatus(TLStatus.PROD);
        tl.setCheckEdited(new Date());
        tl.setTlId(1);

        FileOutputStream fos = new FileOutputStream(file);
        reportService.generateTLReport(tl, fos);

        Assert.assertTrue(file.exists());
    }
}
