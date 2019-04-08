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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityHistory;
import eu.europa.ec.joinup.tsl.business.dto.availability.UnavailabilityReportEntry;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.business.util.UnavailabilityReportExcelGenerator;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

/**
 * Generate EXCEL report with all the unavailability/unsupported session of each production trusted list then send by email if at least one entry
 */
@Service
public class UnavailabilityReportJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnavailabilityReportJobService.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AbstractAlertingService alertingService;

    /**
     * Trigger availability report job
     */
    public void start(Date date) {
        LOGGER.debug("**** START ****");
        List<UnavailabilityReportEntry> unavailabilityStories = getFullDayAvailabilityHistory(date);
        if (!CollectionUtils.isEmpty(unavailabilityStories)) {
            try {
                ByteArrayOutputStream unavailabilityReportOS = new ByteArrayOutputStream();
                UnavailabilityReportExcelGenerator.generateUnavailabilityReport(unavailabilityStories, unavailabilityReportOS);
                alertingService.sendDailyUnavailabilityAlert(date, unavailabilityReportOS);
            } catch (Exception e) {
                LOGGER.error("UnavailabilityReportJobService - error during job", e);
            }
        }
    }

    /**
     * Get unavailability history of production trusted list between start of the day and end of the day
     *
     * @param currentDate
     * @return
     */
    private List<UnavailabilityReportEntry> getFullDayAvailabilityHistory(Date currentDate) {
        List<UnavailabilityReportEntry> tmpAvailabilityStories = new ArrayList<>();
        // Init dates
        Date startOfDay = DateUtils.getStartOfDay(currentDate);
        Date endOfDay = DateUtils.getEndOfDay(currentDate);

        for (DBCountries country : countryService.getAll()) {
            DBTrustedLists dbTL = tlService.getPublishedDbTLByCountry(country);
            if (dbTL == null) {
                LOGGER.error("AVAILABILITY REPORT ALERT - Trusted list null for country : " + country.getCodeTerritory());
            } else {
                AvailabilityHistory tmpHistory = availabilityService.getHistory(dbTL, startOfDay, endOfDay);
                if (!CollectionUtils.isEmpty(tmpHistory.getUnavailableList())) {
                    tmpAvailabilityStories.add(new UnavailabilityReportEntry(dbTL, tmpHistory));
                }
            }
        }
        return tmpAvailabilityStories;
    }

}
