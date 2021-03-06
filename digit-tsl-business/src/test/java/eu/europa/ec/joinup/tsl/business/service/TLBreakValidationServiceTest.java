package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.BreakType;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLCertificateRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCertificate;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@SuppressWarnings("deprecation")
public class TLBreakValidationServiceTest extends AbstractSpringTest {

    @Autowired
    private TLDataLoaderService tlDataLoaderService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TLBreakValidationService tlBreakValidationService;

    @Autowired
    private TLBreakAlertingJobService breakAlertingJobService;

    @Autowired
    private TLCertificateRepository certificateRepository;

    private DBTrustedLists dbTL;

    @Before
    public void init() {
        // LOTL
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
        dbLOTL.setNextUpdateDate(new Date(117, Calendar.DECEMBER, 10, 10, 10));
        tlRepo.save(dbLOTL);

        // TL
        DBCountries country = countryService.getCountryByTerritory("BE");
        dbTL = new DBTrustedLists();
        dbTL.setArchive(false);
        dbTL.setVersionIdentifier(5);
        dbTL.setType(TLType.TL);
        dbTL.setTerritory(country);
        dbTL.setStatus(TLStatus.PROD);
        dbTL.setName("TEST - BE TL");
        DBFiles xmlFile = new DBFiles();
        xmlFile.setMimeTypeFile(MimeType.XML);
        xmlFile.setLocalPath("BE-TEST" + File.separatorChar + "BE_Sn32.xml");
        dbTL.setXmlFile(xmlFile);
        dbTL.setNextUpdateDate(new Date(117, Calendar.NOVEMBER, 10, 10, 10));
        tlRepo.save(dbTL);

        tlDataLoaderService.updateTrustedListData(country);
        tlDataLoaderService.updateLOTLCertificates();
    }

    @After
    public void destroy() {
        tlRepo.deleteAll();
        fileRepository.deleteAll();
        certificateRepository.deleteAll();
    }

    @Test
    public void nextUpdateDateElement() {
        TLBreakStatus breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(116, Calendar.NOVEMBER, 10, 10, 10));
        // Next Update in 1 year
        Assert.assertEquals(365, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertFalse( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertFalse( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.NONE, breakStatus.getBreakType());
        // Next Update in 8 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(117, Calendar.NOVEMBER, 2, 10, 10));
        Assert.assertEquals(8, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertFalse( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        // Next Update in 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(117, Calendar.NOVEMBER, 3, 10, 10));
        Assert.assertEquals(7, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        Assert.assertTrue(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(117, Calendar.NOVEMBER, 3, 10, 10)));
        // Next Update since 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(117, Calendar.NOVEMBER, 17, 10, 10));
        Assert.assertEquals(-7, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertFalse( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
    }

    @Test
    public void signingCertificateDate() {
        dbTL.setNextUpdateDate(new Date(126, Calendar.NOVEMBER, 10, 10, 10));
        tlRepo.save(dbTL);

        TLBreakStatus breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(123, Calendar.FEBRUARY, 17, 10, 10));
        // Signing cert expire in 1 year
        Assert.assertEquals(365, breakStatus.getSigningCertificateElement().getExpireIn());
        Assert.assertFalse( breakStatus.getSigningCertificateElement().isAlert());
        Assert.assertFalse( breakStatus.getSigningCertificateElement().isBreakDay());
        Assert.assertEquals(BreakType.NONE, breakStatus.getBreakType());
        // Signing cert expire in 8 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.FEBRUARY, 9, 10, 10));
        Assert.assertEquals(8, breakStatus.getSigningCertificateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getSigningCertificateElement().isAlert());
        Assert.assertFalse( breakStatus.getSigningCertificateElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        // Signing cert expire in 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.FEBRUARY, 10, 15, 10));
        Assert.assertEquals(7, breakStatus.getSigningCertificateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getSigningCertificateElement().isAlert());
        Assert.assertTrue( breakStatus.getSigningCertificateElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        // Signing cert expired since 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.FEBRUARY, 24, 18, 10));
        Assert.assertEquals(-7, breakStatus.getSigningCertificateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getSigningCertificateElement().isAlert());
        Assert.assertFalse( breakStatus.getSigningCertificateElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Gloabl status ALERT due to next update date expire in 7 days & signing cert expire in 8 days
        dbTL.setNextUpdateDate(new Date(124, Calendar.FEBRUARY, 16, 10, 10));
        tlRepo.save(dbTL);
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.FEBRUARY, 9, 10, 10));
        Assert.assertEquals(8, breakStatus.getSigningCertificateElement().getExpireIn());
        Assert.assertTrue(breakStatus.getSigningCertificateElement().isAlert());
        Assert.assertFalse(breakStatus.getSigningCertificateElement().isBreakDay());
        Assert.assertEquals(7, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
    }

    // ----- ----- Two certificates elements ----- ----- //

    @Test
    public void twoCertificatesElement() {
        dbTL.setNextUpdateDate(new Date(126, Calendar.NOVEMBER, 10, 10, 10));
        tlRepo.save(dbTL);

        TLBreakStatus breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(123, Calendar.FEBRUARY, 11, 10, 10));
        // Two cert condition expire in 2 year
        Assert.assertEquals(731, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.NONE, breakStatus.getBreakType());
        // Two cert condition expire in 60 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.DECEMBER, 13, 10, 10));
        Assert.assertEquals(60, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(1, breakStatus.getTwoCertificatesElement().getCertificatesAffected().size());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        // Two cert condition expire in 8 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY, 3, 10, 10));
        Assert.assertEquals(8, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(1, breakStatus.getTwoCertificatesElement().getCertificatesAffected().size());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        // Two cert condition expire in 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY, 4, 10, 10));
        Assert.assertEquals(7, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(1, breakStatus.getTwoCertificatesElement().getCertificatesAffected().size());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        // Two cert condition NOK
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY, 18, 18, 10));
        Assert.assertEquals(0, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue(breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse(breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Gloabl status ALERT due to next update date expire in 7 days & signing cert expire in 8 days
        dbTL.setNextUpdateDate(new Date(125, Calendar.FEBRUARY,  10, 10, 10));
        tlRepo.save(dbTL);
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY, 3, 10, 10));
        Assert.assertEquals(8, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue(breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse(breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(7, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue(breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertTrue(breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
    }

    @Test
    public void twoCertificatesElementSpecificCases() {
        dbTL.setNextUpdateDate(new Date(199, Calendar.NOVEMBER, 10, 10, 10));
        tlRepo.save(dbTL);
        List<DBCertificate> certs = certificateRepository.getAllByCountryCodeAndTlType("BE", TLType.LOTL);
        // Two certs start the day of the expiration of the "current last one"

        // Add one certificate 02/11/2025 - 02/03/2025.As TL will not break the 02/11/2025
        DBCertificate dbc1 = new DBCertificate();
        dbc1.setBase64("azer");
        dbc1.setCountryCode("BE");
        dbc1.setNotBefore(new Date(125, Calendar.FEBRUARY,  11, 15, 0));
        dbc1.setNotAfter(new Date(128, Calendar.FEBRUARY, 3, 10, 10));
        dbc1.setSubjectName("TEST 1");
        dbc1.setSki("ABC".getBytes());

        dbc1.setTlType(TLType.LOTL);
        certificateRepository.save(dbc1);

        // Due to new cert, no break
        TLBreakStatus breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.JANUARY, 11, 18, 10));
        Assert.assertEquals(150, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertFalse(breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse(breakStatus.getTwoCertificatesElement().isBreakDay());
        // Warning due to signing certificate expiration
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Second last expire in 90 days, status will break
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.MARCH, 12, 18, 10));
        Assert.assertEquals(90, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue(breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertTrue(breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.MARCH, 12, 18, 10));

        // Add one certificate 06/11/2025 - 02/03/2025.As TL will not break the 06/11/2025
        DBCertificate dbc2 = new DBCertificate();
        dbc2.setBase64("azer");
        dbc2.setCountryCode("BE");
        dbc2.setNotBefore(new Date(125, Calendar.FEBRUARY,  11, 16, 0));
        dbc2.setNotAfter(new Date(128, Calendar.FEBRUARY,  3, 10, 10));
        dbc2.setSubjectName("TEST 2");
        dbc2.setTlType(TLType.LOTL);
        dbc2.setSki("ABC".getBytes());
        certificateRepository.save(dbc2);

        // Check 7 days before expiration of those 2 new certs
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(128, Calendar.JANUARY, 27, 1, 10));
        Assert.assertEquals(7, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(2, breakStatus.getTwoCertificatesElement().getCertificatesAffected().size());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(128, Calendar.JANUARY, 27, 1, 10));

        // ----- ----- END OF CASE ----- ----- //

        certificateRepository.delete(dbc1);
        certificateRepository.delete(dbc2);

        DBCertificate cloneOfCert3 = new DBCertificate();
        cloneOfCert3.setBase64(certs.get(3).getBase64());
        cloneOfCert3.setCountryCode(certs.get(3).getCountryCode());
        cloneOfCert3.setNotAfter(certs.get(3).getNotAfter());
        cloneOfCert3.setNotBefore(certs.get(3).getNotBefore());
        cloneOfCert3.setSubjectName("CLONE TEST");
        cloneOfCert3.setTlType(certs.get(3).getTlType());
        cloneOfCert3.setSki("ABC".getBytes());
        cloneOfCert3 = certificateRepository.save(cloneOfCert3);

        // Two cert condition expire in 60 days. Two certs concerned
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(124, Calendar.DECEMBER, 13, 10, 10));
        Assert.assertEquals(60, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(2, breakStatus.getTwoCertificatesElement().getCertificatesAffected().size());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());

        // Remove cert 3 (2025/02/11) + clone
        certificateRepository.delete(certs.get(3));
        certificateRepository.delete(cloneOfCert3);

        // Two cert condition NOK.
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.JUNE, 16, 18, 10));
        Assert.assertEquals(0, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Two cert condition NOK
        certificateRepository.delete(certs.get(2));
        certificateRepository.delete(certs.get(1));
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.JUNE, 16, 18, 10));
        Assert.assertEquals(0, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        Assert.assertTrue(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.JULY, 1, 18, 10)));

        // Only one cert, condition never respected
        certificateRepository.delete(certs.get(0));
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.JUNE, 16, 18, 10));
        Assert.assertEquals(0, breakStatus.getTwoCertificatesElement().getExpireIn());
        Assert.assertTrue( breakStatus.getTwoCertificatesElement().isAlert());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isVerifiable());
        Assert.assertFalse( breakStatus.getTwoCertificatesElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        Assert.assertTrue(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.JULY, 1, 18, 10)));

    }

    // ----- ----- Shifted Validity Period ----- ----- //

    @Test
    public void shiftedValidityPeriod() {
        dbTL.setNextUpdateDate(new Date(126, Calendar.NOVEMBER, 10, 10, 10));
        tlRepo.save(dbTL);

        TLBreakStatus breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(123, Calendar.FEBRUARY, 11, 10, 10));
        // Shifted validity period expire in 2 year
        Assert.assertEquals(731, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(BreakType.NONE, breakStatus.getBreakType());
        // Shifted validity period expire in 8 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  3, 10, 10));
        Assert.assertEquals(8, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        // Shifted validity period expire in 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  4, 10, 10));
        Assert.assertEquals(7, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());
        // Shifted validity period expired since 7 days
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  18, 18, 10));
        Assert.assertEquals(-7, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Gloabl status ALERT due to next update date expire in 7 days & shifted validity period expire in 8 days
        dbTL.setNextUpdateDate(new Date(125, Calendar.FEBRUARY,  10, 10, 10));
        tlRepo.save(dbTL);
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  3, 10, 10));
        Assert.assertEquals(8, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(7, breakStatus.getNextUpdateDateElement().getExpireIn());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isAlert());
        Assert.assertTrue( breakStatus.getNextUpdateDateElement().isBreakDay());
        Assert.assertEquals(BreakType.DAY_OF_BREAK, breakStatus.getBreakType());

        // Add a new certificate that expire 1 month after the last one
        DBCertificate certificate = new DBCertificate();
        certificate.setCountryCode("BE");
        certificate.setNotBefore(new Date(110, Calendar.FEBRUARY, 1, 1, 1));
        certificate.setNotAfter(new Date(125, Calendar.JULY, 11, 18, 10));
        certificate.setSubjectName("TEST CERTIFICATE");
        certificate.setTlType(TLType.LOTL);
        certificate.setBase64("fghjkl");
        certificate.setSki("ABC".getBytes());
        certificate = certificateRepository.save(certificate);
        dbTL.setNextUpdateDate(new Date(128, Calendar.FEBRUARY,  10, 10, 10));
        tlRepo.save(dbTL);

        // Shifted validity period expired since 7 days even with 2 certificate not expired
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  18, 18, 10));
        Assert.assertEquals(-7, breakStatus.getShiftedPeriodElement().getExpireIn());
        Assert.assertTrue( breakStatus.getShiftedPeriodElement().isAlert());
        Assert.assertFalse( breakStatus.getShiftedPeriodElement().isBreakDay());
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());

        // Mail
        dbTL.setNextUpdateDate(new Date(130, Calendar.FEBRUARY, 10, 10, 10));
        // None
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(123, Calendar.FEBRUARY, 18, 18, 10));
        Assert.assertEquals(BreakType.NONE, breakStatus.getBreakType());
        Assert.assertFalse(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(123, Calendar.FEBRUARY, 18, 18, 10)));
        // Warning but not 1st of the month
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.FEBRUARY,  18, 18, 10));
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        Assert.assertFalse(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.FEBRUARY,  18, 18, 10)));
        // Warning and 1st of the month
        breakStatus = tlBreakValidationService.initTLBreakStatus(dbTL, new Date(125, Calendar.MARCH, 1, 18, 10));
        Assert.assertEquals(BreakType.WARNING, breakStatus.getBreakType());
        Assert.assertTrue(breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.MARCH, 1, 18, 10)));
    }

    @Test
    public void alertMail() {
        dbTL.setNextUpdateDate(new Date(125, Calendar.FEBRUARY,  20, 10, 10));
        tlRepo.save(dbTL);
        // DayBreak
        // breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(125, Calendar.FEBRUARY,  10, 10, 10));
        // First of the month
        breakAlertingJobService.tlStatusBreakAlert(dbTL, new Date(126, Calendar.FEBRUARY, 1, 10, 10));
    }

}
