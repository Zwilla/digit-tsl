package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityHistory;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityPieChart;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityState;
import eu.europa.ec.joinup.tsl.business.repository.AvailabilityRepository;
import eu.europa.ec.joinup.tsl.business.repository.FileRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public class AvailabilityServiceTest extends AbstractSpringTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private TLRepository tlRepo;

    @Autowired
    private CountryService countryService;

    private DBTrustedLists tl = new DBTrustedLists();

    Date dMin = null;
    Date dMax = null;

    @Before
    @SuppressWarnings("deprecation")
    public void init() {
        DBFiles f1 = new DBFiles();
        f1.setDigest("");
        f1.setFirstScanDate(new Date(116, Calendar.OCTOBER, 25, 11, 25));
        f1.setLocalPath("LU" + File.separatorChar + "2016-10-13_12-56-25.xml");
        f1.setMimeTypeFile(MimeType.XML);
        f1.setUrl("");
        f1.setAvailabilityInfos(new ArrayList<DBFilesAvailability>());

        DBFilesAvailability a0 = new DBFilesAvailability();
        a0.setCheckDate(new Date(116, Calendar.OCTOBER, 2, 8, 10));
        a0.setFile(f1);
        a0.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a1 = new DBFilesAvailability();
        a1.setCheckDate(new Date(116, Calendar.OCTOBER, 25, 11, 26));
        a1.setFile(f1);
        a1.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a2 = new DBFilesAvailability();
        a2.setCheckDate(new Date(116, Calendar.OCTOBER, 29, 18, 20));
        a2.setFile(f1);
        a2.setStatus(AvailabilityStatus.UNAVAILABLE);

        DBFilesAvailability a3 = new DBFilesAvailability();
        a3.setCheckDate(new Date(116, Calendar.OCTOBER, 30, 10, 10));
        a3.setFile(f1);
        a3.setStatus(AvailabilityStatus.AVAILABLE);

        f1.getAvailabilityInfos().add(a0);
        f1.getAvailabilityInfos().add(a1);
        f1.getAvailabilityInfos().add(a2);
        f1.getAvailabilityInfos().add(a3);

        DBCountries c = countryService.getCountryByTerritory("LU");

        tl.setName("TL_AVA");
        tl.setArchive(false);
        tl.setXmlFile(f1);
        tl.setTerritory(c);
        tl.setStatus(TLStatus.PROD);
        tl.setSequenceNumber(200);
        tl.setVersionIdentifier(5);
        tl = tlRepo.save(tl);

        dMin = new Date(116, Calendar.SEPTEMBER, 20, 12, 0);
        dMax = new Date(116, Calendar.OCTOBER, 30, 0, 1);
    }

    @After
    public void destroy() {
        availabilityRepository.deleteAll();
        tlRepo.deleteAll();
        fileRepository.deleteAll();
    }

    @Test
    public void testChangeAvailability() {
        DBFiles file = new DBFiles();
        fileRepository.save(file);

        assertNull(availabilityRepository.findTopByFileIdOrderByCheckDateDesc(file.getId()));

        availabilityService.setAvailable(file);
        DBFilesAvailability latest = availabilityRepository.findTopByFileIdOrderByCheckDateDesc(file.getId());
        assertNotNull(latest);
        assertEquals(AvailabilityStatus.AVAILABLE, latest.getStatus());

        availabilityService.setAvailable(file);

        availabilityService.setUnavailable(file);

        latest = availabilityRepository.findTopByFileIdOrderByCheckDateDesc(file.getId());
        assertNotNull(latest);
        assertEquals(AvailabilityStatus.UNAVAILABLE, latest.getStatus());

    }

    @Test
    @SuppressWarnings("deprecation")
    public void getFullHistory() {
        AvailabilityHistory history = availabilityService.getHistory(tl, new Date(110, Calendar.OCTOBER, 2, 8, 10), dMax);
        Assert.assertNotNull(history);
    }

    @Test
    @Transactional
    public void calculAvailability() {
        // Get DBFiles entires
        List<DBFiles> tlFiles = fileService.getProductionTLFilesByTerritoryOrderByFirstScanDate(tl.getTerritory());
        List<DBFilesAvailability> dbFilesAvailability = null;
        try {
            dbFilesAvailability = availabilityService.getAvailabilityEntriesOrderer(tlFiles, null, null);
        } catch (IllegalStateException e) {
            Assert.assertEquals("Both, 'Start date' and 'End date' must be defined.", e.getMessage());
        }
        Assert.assertNull(dbFilesAvailability);
        dbFilesAvailability = availabilityService.getAvailabilityEntriesOrderer(tlFiles, new Date(), new Date());
        Assert.assertNotNull(dbFilesAvailability);
        Assert.assertEquals(1, dbFilesAvailability.size());
        dbFilesAvailability = availabilityService.getAvailabilityEntriesOrderer(tlFiles, dMin, dMax);
        Assert.assertNotNull(dbFilesAvailability);
        Assert.assertEquals(3, dbFilesAvailability.size());

        // Calcul unavailable timing
        List<AvailabilityState> unavailables = availabilityService.getAvailabilityStates(dbFilesAvailability, dMax);
        Assert.assertNotNull(unavailables);
        Assert.assertEquals(2, unavailables.size());
        Assert.assertEquals(AvailabilityStatus.UNAVAILABLE, unavailables.get(0).getStatus());
        long diff = unavailables.get(0).getEndDate().getTime() - unavailables.get(0).getStartDate().getTime();
        Assert.assertEquals(diff, 20460000);

        // Set pie chart info
        AvailabilityPieChart pieChart = availabilityService.getAvailabilityPieChart(unavailables);
        Assert.assertEquals(pieChart.getAvailableTiming(), 3482400000L);
        Assert.assertEquals(pieChart.getUnavailableTiming(), 20460000L);
        Assert.assertEquals(pieChart.getUnsupportedTiming(), 0);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getCurrentStatusDuration() {
        AvailabilityState unavailableTiming = availabilityService.getCurrentStatusDuration(getFile().getId());
        Assert.assertEquals(AvailabilityStatus.AVAILABLE, unavailableTiming.getStatus());
        Assert.assertEquals(new Date(116, Calendar.NOVEMBER, 30, 10, 10), unavailableTiming.getStartDate());
    }

    @Test
    @Transactional
    @SuppressWarnings("deprecation")
    public void unavailabilityAlertVerification() {
        DBFiles dbFile = getFile();
        Assert.assertFalse(availabilityService.unavailabilityAlertVerification(dbFile.getId()));

        DBFilesAvailability a1 = new DBFilesAvailability();
        a1.setCheckDate(new Date(117, Calendar.AUGUST, 30, 10, 30));
        a1.setFile(dbFile);
        a1.setStatus(AvailabilityStatus.UNAVAILABLE);

        dbFile.getAvailabilityInfos().add(a1);
        fileRepository.save(dbFile);

        Assert.assertTrue(availabilityService.unavailabilityAlertVerification(dbFile.getId()));
    }

    @Test
    @Transactional
    @SuppressWarnings("deprecation")
    public void triggerAlerting() {
        DBFiles dbFile = getFile();
        Assert.assertFalse(availabilityService.triggerAlerting(dbFile.getId()));

        DBFilesAvailability a1 = new DBFilesAvailability();
        a1.setCheckDate(new Date(117, Calendar.AUGUST, 30, 10, 30));
        a1.setFile(dbFile);
        a1.setStatus(AvailabilityStatus.UNAVAILABLE);

        dbFile.getAvailabilityInfos().add(a1);
        fileRepository.save(dbFile);

        Assert.assertTrue(availabilityService.triggerAlerting(dbFile.getId()));
        Assert.assertFalse(availabilityService.triggerAlerting(dbFile.getId()));
    }

    private DBFiles getFile() {
        List<DBFiles> dbFiles = (List<DBFiles>) fileRepository.findAll();
        return dbFiles.get(0);
    }
}
