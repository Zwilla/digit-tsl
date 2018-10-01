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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.NotificationPointers;
import eu.europa.ec.joinup.tsl.business.dto.contact.ContactList.TLSOContact;
import eu.europa.ec.joinup.tsl.business.dto.notification.MemberStateNotificationV5;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.repository.NotificationRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.rules.DigitalIdentityValidator;
import eu.europa.ec.joinup.tsl.business.util.TLDigitalIdentityUtils;
import eu.europa.ec.joinup.tsl.business.util.TLNotificationMapper;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBNotification;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.NotificationStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotificationServiceTest extends AbstractSpringTest {

    @Value("${notification.shift.period}")
    private int notificationShiftPeriod;

    private ByteArrayOutputStream out = null;
    private NotificationPointers notification = new NotificationPointers();
    private static final String draftStoreId = "45121sfd5f45e4z1ds1sd4s5ds1d5s4d";

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationJaxbService notificationJaxbService;

    @Autowired
    private DigitalIdentityValidator digitalIdValidator;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TLLoader tlLoader;

    @Before
    public void init() {
        notification.setDateOfEffect(new Date());
        notification.setDateOfSubmission(new Date());
        List<String> listCert = new ArrayList<>();
        listCert.add("t1");
        listCert.add("t2");
        notification.setAddedCertificates(listCert);
        notification.setDeletedCertificates(listCert);
        notification.setTlsoContact(new TLSOContact());
        notification.setCreationDate(new Date());

        TLPointersToOtherTSL pointer = new TLPointersToOtherTSL();
        pointer.setId("0");
        pointer.setMimeType(MimeType.XML);
        pointer.setSchemeOpeName(new ArrayList<TLName>());
        pointer.setSchemeTerritory("BE");
        pointer.setTlLocation("location");
        pointer.setTlType("type");

        notification.setMpPointer(pointer);
        notification.setHrPointer(pointer);

        // DbNotification
        DBNotification dbNotif = new DBNotification();
        DBCountries country = new DBCountries();
        country.setCodeTerritory("BE");
        country.setCountryName("Belgium");
        dbNotif.setId(1);
        dbNotif.setTerritory(country);
        dbNotif.setSubmissionDate(new Date());
        dbNotif.setStatus(NotificationStatus.RECEIVED);
        dbNotif.setInsertDate(new Date());
        dbNotif.setIdentifier("identifier");
        dbNotif.setDraftStoreId(draftStoreId);
        dbNotif.setContactChange(false);
        DBFiles notificationFile = new DBFiles();

        notificationFile.setId(1);
        notificationFile.setUrl("url");
        notificationFile.setMimeTypeFile(MimeType.XML);
        notificationFile.setLocalPath("src/test/resources/notification.xml");
        notificationFile.setLastScanDate(new Date());
        dbNotif.setNotificationFile(notificationFile);
        dbNotif.setReportFile(new DBFiles());
        notificationRepository.save(dbNotif);
    }

    @Test
    public void AgetOS() throws IOException {
        out = notificationService.getNotificationOs(notification);
        try (OutputStream outputStream = new FileOutputStream("src/test/resources/test-notif.xml")) {
            out.writeTo(outputStream);
        }
        Assert.assertNotNull(out);
    }

    @Test
    public void BcreateDraftFromBinaries() {

        try {
            DBNotification dbNotif = notificationService.persistNotification(out.toByteArray(), out.toByteArray(), draftStoreId);
            Assert.assertNotNull(dbNotif);
            Assert.assertEquals(dbNotif.getDraftStoreId(), draftStoreId);
            Assert.assertEquals(dbNotif.getTerritory().getCodeTerritory().toString(), "BE");
            Assert.assertEquals(dbNotif.getStatus().toString(), "RECEIVED");
        } catch (Exception e) {
        }
    }

    @Test
    public void CupdateStatus() {
        DBNotification dbNotif = notificationRepository.findOne(1);
        Assert.assertNotNull(dbNotif);
        Assert.assertEquals(NotificationStatus.RECEIVED, dbNotif.getStatus());
        notificationService.updateStatus(dbNotif, NotificationStatus.VALIDATED);
        DBNotification dbNotifUpdated = notificationRepository.findOne(1);
        Assert.assertEquals(NotificationStatus.VALIDATED, dbNotifUpdated.getStatus());
    }

    @Test
    public void DsetContactChange() {
        DBNotification dbNotif = notificationRepository.findOne(1);
        Assert.assertNotNull(dbNotif);
        Assert.assertEquals(false, dbNotif.isContactChange());
        notificationService.updateStatus(dbNotif, NotificationStatus.VALIDATED);
        DBNotification dbNotifUpdated = notificationRepository.findOne(1);
        Assert.assertEquals(NotificationStatus.VALIDATED, dbNotifUpdated.getStatus());
    }

    @Test
    public void EgetXmlFile() {
        File f = notificationService.getNotificationXmlFile(1);
        Assert.assertNotNull(f);
    }

    @Test
    public void FuploadNotif() throws Exception {
        Path path = Paths.get("src/test/resources/be-notif.xml");
        byte[] b = Files.readAllBytes(path);
        MemberStateNotificationV5 memberNotif = notificationJaxbService.unmarshallNotification(b);
        Load load = new Load();
        tlLoader.loadTL("EU", getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);
        NotificationPointers prodPointer = notificationService.getNotification(memberNotif.getSchemeContact().getTerritory());
        NotificationPointers notifPointer = TLNotificationMapper.mapXMLNotificationObjToDTONotification(memberNotif, prodPointer);
        Assert.assertNotNull(notifPointer);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void GtestCertificates() {
        tlRepo.deleteAll();
        DBCountries lotlCountry = countryService.getCountryByTerritory("EU");
        DBTrustedLists dbLOTL = new DBTrustedLists();
        dbLOTL.setArchive(false);
        dbLOTL.setVersionIdentifier(5);
        dbLOTL.setType(TLType.LOTL);
        dbLOTL.setTerritory(lotlCountry);
        dbLOTL.setStatus(TLStatus.PROD);
        dbLOTL.setName("TEST - LOTL");
        DBFiles lotlXmlFile = new DBFiles();
        lotlXmlFile.setMimeTypeFile(MimeType.XML);
        lotlXmlFile.setLocalPath("EU" + File.separatorChar + "EU_Sn182.xml");
        dbLOTL.setXmlFile(lotlXmlFile);
        dbLOTL.setNextUpdateDate(new Date(117, 11, 10, 10, 10));
        tlRepo.save(dbLOTL);

        NotificationPointers notif = notificationService.getNotification("BE");

        //OK
        Assert.assertTrue(digitalIdValidator.isTwoCertificatesNotExpired(TLDigitalIdentityUtils.retrieveCertificates(notif.getMpPointer().getServiceDigitalId()),
                new Date(117, 8, 26, 12, 00)));
        Assert.assertTrue(
                digitalIdValidator.isShiftedPeriodValid(TLDigitalIdentityUtils.retrieveCertificates(notif.getMpPointer().getServiceDigitalId()), new Date(117, 8, 26, 12, 00)));

        //Still OK
        Assert.assertTrue(digitalIdValidator.isTwoCertificatesNotExpired(TLDigitalIdentityUtils.retrieveCertificates(notif.getMpPointer().getServiceDigitalId()),
                new Date(125, 0, 1, 12, 00)));
        Assert.assertTrue(
                digitalIdValidator.isShiftedPeriodValid(TLDigitalIdentityUtils.retrieveCertificates(notif.getMpPointer().getServiceDigitalId()), new Date(124, 9, 1, 12, 00)));

        //Less than 4 months before the expiration of shift period
        Assert.assertFalse(digitalIdValidator.isShiftedPeriodValid(TLDigitalIdentityUtils.retrieveCertificates(notif.getMpPointer().getServiceDigitalId()),
                getVerifDay(new Date(125, 0, 1, 12, 00))));

    }

    //Add notification shift period to check date
    private Date getVerifDay(Date checkDate) {
        Calendar calV = Calendar.getInstance();
        calV.setTime(checkDate);
        calV.add(Calendar.DAY_OF_MONTH, notificationShiftPeriod);
        return calV.getTime();
    }

}
