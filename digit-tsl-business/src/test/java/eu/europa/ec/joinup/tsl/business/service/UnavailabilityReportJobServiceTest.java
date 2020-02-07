package eu.europa.ec.joinup.tsl.business.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

public class UnavailabilityReportJobServiceTest extends AbstractSpringTest {

    @Autowired
    private UnavailabilityReportJobService unavailabilityReportJobService;

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
        f1.setFirstScanDate(new Date(117, Calendar.NOVEMBER, 10, 1, 0));
        f1.setLocalPath("LU" + File.separatorChar + "2016-10-13_12-56-25.xml");
        f1.setMimeTypeFile(MimeType.XML);
        f1.setUrl("");
        f1.setAvailabilityInfos(new ArrayList<DBFilesAvailability>());

        DBFilesAvailability a = new DBFilesAvailability();
        a.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 1, 0));
        a.setFile(f1);
        a.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a0 = new DBFilesAvailability();
        a0.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 2, 0));
        a0.setFile(f1);
        a0.setStatus(AvailabilityStatus.UNSUPPORTED);

        DBFilesAvailability a1 = new DBFilesAvailability();
        a1.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 10, 0));
        a1.setFile(f1);
        a1.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a2 = new DBFilesAvailability();
        a2.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 16, 0));
        a2.setFile(f1);
        a2.setStatus(AvailabilityStatus.UNAVAILABLE);

        DBFilesAvailability a3 = new DBFilesAvailability();
        a3.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 20, 0));
        a3.setFile(f1);
        a3.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a4 = new DBFilesAvailability();
        a4.setCheckDate(new Date(117, Calendar.NOVEMBER, 10, 23, 0));
        a4.setFile(f1);
        a4.setStatus(AvailabilityStatus.UNAVAILABLE);

        f1.getAvailabilityInfos().add(a);
        f1.getAvailabilityInfos().add(a0);
        f1.getAvailabilityInfos().add(a1);
        f1.getAvailabilityInfos().add(a2);
        f1.getAvailabilityInfos().add(a3);
        f1.getAvailabilityInfos().add(a4);

        DBCountries c = countryService.getCountryByTerritory("LU");

        tl.setName("TL_AVA");
        tl.setArchive(false);
        tl.setXmlFile(f1);
        tl.setTerritory(c);
        tl.setStatus(TLStatus.PROD);
        tl.setSequenceNumber(200);
        tl.setVersionIdentifier(5);
        tl = tlRepo.save(tl);

        dMin = new Date(116, Calendar.OCTOBER, 20, 12, 0);
        dMax = new Date(116, Calendar.NOVEMBER, 30, 0, 1);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void unavailabilityReportJobService() {
        Date today = new Date(117, Calendar.NOVEMBER, 10, 12, 0);
        try {
            unavailabilityReportJobService.start(today);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
