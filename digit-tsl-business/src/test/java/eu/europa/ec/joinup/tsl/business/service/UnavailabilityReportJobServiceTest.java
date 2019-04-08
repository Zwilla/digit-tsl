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
import java.util.ArrayList;
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
        f1.setFirstScanDate(new Date(117, 10, 10, 01, 00));
        f1.setLocalPath("LU" + File.separatorChar + "2016-10-13_12-56-25.xml");
        f1.setMimeTypeFile(MimeType.XML);
        f1.setUrl("");
        f1.setAvailabilityInfos(new ArrayList<DBFilesAvailability>());

        DBFilesAvailability a = new DBFilesAvailability();
        a.setCheckDate(new Date(117, 10, 10, 01, 00));
        a.setFile(f1);
        a.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a0 = new DBFilesAvailability();
        a0.setCheckDate(new Date(117, 10, 10, 02, 00));
        a0.setFile(f1);
        a0.setStatus(AvailabilityStatus.UNSUPPORTED);

        DBFilesAvailability a1 = new DBFilesAvailability();
        a1.setCheckDate(new Date(117, 10, 10, 10, 00));
        a1.setFile(f1);
        a1.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a2 = new DBFilesAvailability();
        a2.setCheckDate(new Date(117, 10, 10, 16, 00));
        a2.setFile(f1);
        a2.setStatus(AvailabilityStatus.UNAVAILABLE);

        DBFilesAvailability a3 = new DBFilesAvailability();
        a3.setCheckDate(new Date(117, 10, 10, 20, 00));
        a3.setFile(f1);
        a3.setStatus(AvailabilityStatus.AVAILABLE);

        DBFilesAvailability a4 = new DBFilesAvailability();
        a4.setCheckDate(new Date(117, 10, 10, 23, 00));
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

        dMin = new Date(116, 9, 20, 12, 00);
        dMax = new Date(116, 10, 30, 00, 01);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void unavailabilityReportJobService() {
        Date today = new Date(117, 10, 10, 12, 00);
        try {
            unavailabilityReportJobService.start(today);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }
    }

}
