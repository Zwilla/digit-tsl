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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.availability.Availability;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityHistory;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityPieChart;
import eu.europa.ec.joinup.tsl.business.dto.availability.AvailabilityState;
import eu.europa.ec.joinup.tsl.business.repository.AvailabilityRepository;
import eu.europa.ec.joinup.tsl.business.util.AvailabilityComparatorUtils;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBFilesAvailability;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AvailabilityStatus;

@Service
@Transactional(value = TxType.REQUIRED)
public class AvailabilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilityService.class);

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLService tlService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Value("${timing.availability}")
    private int availabilityTiming;

    // ----- ----- AVAILABILITY STATUS ----- ----- //

    /**
     * This method adds an AVAILABLE status for the given file if the previous record is not AVAILABLE
     */
    public void setAvailable(DBFiles file) {
        addIfNotCurrentStatus(file, AvailabilityStatus.AVAILABLE);
    }

    /**
     * This method adds an UNAVAILABLE status for the given file if the previous record is not UNAVAILABLE
     */
    public void setUnavailable(DBFiles file) {
        addIfNotCurrentStatus(file, AvailabilityStatus.UNAVAILABLE);
    }

    /**
     * This method adds an UNSUPPORTED status for the given file if the previous record is not UNSUPPORTED
     */
    public void setUnsupported(DBFiles file) {
        addIfNotCurrentStatus(file, AvailabilityStatus.UNSUPPORTED);
    }

    /**
     * Add new availability entry when status change
     *
     * @param file
     * @param status
     */
    private void addIfNotCurrentStatus(DBFiles file, AvailabilityStatus status) {
        DBFilesAvailability latestAvailabilty = availabilityRepository.findTopByFileIdOrderByCheckDateDesc(file.getId());
        if ((latestAvailabilty == null) || !status.equals(latestAvailabilty.getStatus())) {
            DBFilesAvailability newAvailability = new DBFilesAvailability();
            newAvailability.setStatus(status);
            newAvailability.setFile(file);
            newAvailability.setCheckDate(new Date());
            availabilityRepository.save(newAvailability);
        }
    }

    // ----- ----- AVAILABILITY CHARTS ----- ----- //

    /**
     * Get all the availability informations (pie chart, bar chart and data table)
     *
     * @param tl
     * @param dMin
     * @param dMax
     * @return
     */
    public AvailabilityHistory getHistory(DBTrustedLists tl, Date dMin, Date dMax) {
        if ((dMin == null) || (dMax == null)) {
            throw new IllegalStateException(bundle.getString("error.availability.request.date.undefined"));
        }
        AvailabilityHistory myHistory = new AvailabilityHistory();
        // Get Availability entries
        List<DBFiles> tlFiles = fileService.getProductionTLFilesByTerritoryOrderByFirstScanDate(tl.getTerritory());
        List<DBFilesAvailability> dbFilesAvailability = getAvailabilityEntriesOrderer(tlFiles, dMin, dMax);

        //
        List<AvailabilityState> availabilityStateList = getAvailabilityStates(dbFilesAvailability, dMax);

        myHistory.setUnavailableList(availabilityStateList);
        myHistory.setAvailabilityChartList(getAvailabilityChart(availabilityStateList));
        myHistory.setAvailabilityPieChart(getAvailabilityPieChart(availabilityStateList));
        return myHistory;
    }

    /**
     * Get availability status timing line
     */
    public List<AvailabilityState> getAvailabilityStates(List<DBFilesAvailability> availabilityDbList, Date dMax) {
        List<AvailabilityState> unavailableTiming = new ArrayList<>();

        if (!CollectionUtils.isEmpty(availabilityDbList)) {
            // 1st entry
            AvailabilityState tmpEntry = new AvailabilityState();
            tmpEntry.setStatus(availabilityDbList.get(0).getStatus());
            tmpEntry.setStartDate(availabilityDbList.get(0).getCheckDate());
            tmpEntry.setEndDate(dMax);

            // Loop through list
            for (int i = 1; i < availabilityDbList.size(); i++) {
                if (tmpEntry.getStatus().equals(availabilityDbList.get(i).getStatus())) {
                    tmpEntry.setStartDate(availabilityDbList.get(i).getCheckDate());
                } else {
                    // Back-up endDate and add to list
                    Date endDate = tmpEntry.getStartDate();
                    unavailableTiming.add(tmpEntry);
                    // Init new entry
                    tmpEntry = new AvailabilityState();
                    tmpEntry.setStatus(availabilityDbList.get(i).getStatus());
                    tmpEntry.setStartDate(availabilityDbList.get(i).getCheckDate());
                    tmpEntry.setEndDate(endDate);
                }
            }
            unavailableTiming.add(tmpEntry);
        }
        return unavailableTiming;
    }

    /**
     * Get Availability information for boolean line chart (available/not available)
     *
     * @param dMax
     */
    public List<Availability> getAvailabilityChart(List<AvailabilityState> availabilityStateList) {
        List<Availability> tmpChartList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(availabilityStateList)) {
            for (AvailabilityState state : availabilityStateList) {
                // Create two points by entry
                Availability entryEnd = new Availability(state.getStatus());
                entryEnd.setCheckDate(state.getEndDate());
                Availability entryStart = new Availability(state.getStatus());
                entryStart.setCheckDate(state.getStartDate());
                // Add entries
                tmpChartList.add(entryEnd);
                tmpChartList.add(entryStart);
            }
        }
        return tmpChartList;
    }

    /**
     * Init availability pie chart informations. Get the time of each AvailabilityState
     *
     * @param availabilityStateList
     * @return
     */
    public AvailabilityPieChart getAvailabilityPieChart(List<AvailabilityState> availabilityStateList) {
        AvailabilityPieChart pieChartData = new AvailabilityPieChart();
        if (!CollectionUtils.isEmpty(availabilityStateList)) {
            for (AvailabilityState state : availabilityStateList) {
                pieChartData.addTime(state);
            }
        }
        return pieChartData;
    }

    /**
     * Loop through XML files, get availability and filter them based on a given period sorted by oldest to newest entry
     *
     * @param xmlFiles
     * @param dMin
     * @param dMax
     * @return
     */
    public List<DBFilesAvailability> getAvailabilityEntriesOrderer(List<DBFiles> xmlFiles, Date dMin, Date dMax) {
        if ((dMin == null) || (dMax == null)) {
            throw new IllegalStateException(bundle.getString("error.availability.request.date.undefined"));
        }
        List<DBFilesAvailability> availabilityFullDbList = new ArrayList<>();
        List<DBFilesAvailability> availabilityResultList = new ArrayList<>();
        for (DBFiles xmlFile : xmlFiles) {
            availabilityFullDbList.addAll(xmlFile.getAvailabilityInfos());
            // Availability entries between dMin & dMax
            availabilityResultList.addAll(availabilityRepository.findBetweenTwoDate(xmlFile.getId(), dMin, dMax));
        }

        // No result at all
        if (CollectionUtils.isEmpty(availabilityFullDbList)) {
            return Collections.emptyList();
        }

        // Force sort by check date
        Collections.sort(availabilityFullDbList, new AvailabilityComparatorUtils());
        Collections.sort(availabilityResultList, new AvailabilityComparatorUtils());

        if (availabilityFullDbList.size() != availabilityResultList.size()) {
            // Check if there is a gap between oldest entry before dMin and dMin
            if (CollectionUtils.isEmpty(availabilityResultList) || (availabilityResultList.get(availabilityResultList.size() - 1).getCheckDate().after(dMin))) {
                // Find the gap
                DBFilesAvailability missingGap = getAvailabilityGap(dMin, availabilityFullDbList);
                if (missingGap != null) {
                    // Add missing gap at last position
                    availabilityResultList.add(missingGap);
                }
            }
        }
        return availabilityResultList;
    }

    /**
     * Find first entry before dMin date and return value with checkDate set to dMin OR return null if no value found
     *
     * @param dMin
     * @param availabilityFullDbList
     */

    private DBFilesAvailability getAvailabilityGap(Date dMin, List<DBFilesAvailability> availabilityFullDbList) {
        // Init Params
        Boolean valueFind = false;
        int index = 0;
        while (!valueFind && (index < (availabilityFullDbList.size()))) {
            if (availabilityFullDbList.get(index).getCheckDate().before(dMin)) {
                DBFilesAvailability missingGap = new DBFilesAvailability();
                missingGap.setStatus(availabilityFullDbList.get(index).getStatus());
                missingGap.setCheckDate(dMin);
                missingGap.setFile(availabilityFullDbList.get(index).getFile());
                return missingGap;
            }
            index = index + 1;
        }
        return null;
    }

    // ----- ----- ALERTING ----- ----- //

    /**
     * Get last entry of a specified DB_FILES start date and end date
     *
     * @param countryCode
     * @return
     */
    public AvailabilityState getCurrentStatusDuration(int fileId) {
        DBFiles tlFiles = fileService.getFileById(fileId);
        List<DBFilesAvailability> tmpAvailability = new ArrayList<>(tlFiles.getAvailabilityInfos());
        if (CollectionUtils.isEmpty(tmpAvailability)) {
            return null;
        }
        // Init variables
        AvailabilityStatus currentStatus = tmpAvailability.get(0).getStatus();
        AvailabilityState currentStatusTiming = new AvailabilityState();
        currentStatusTiming.setStatus(currentStatus);
        currentStatusTiming.setStartDate(tmpAvailability.get(0).getCheckDate());
        currentStatusTiming.setEndDate(new Date());
        if (tmpAvailability.size() > 1) {
            // Init n+1 value
            int index = 0;
            DBFilesAvailability prevStatus = tmpAvailability.get(index);
            while (prevStatus.getStatus().equals(currentStatus) && (index < tmpAvailability.size())) {
                currentStatusTiming.setStartDate(tmpAvailability.get(index).getCheckDate());
                index = index + 1;
                prevStatus = tmpAvailability.get(index);
            }
        }
        return currentStatusTiming;
    }

    /**
     * Get current availability status, calcul duration & return true if difference is bigger than @availabilityTiming
     *
     * @param countryCode
     * @return
     */
    public Boolean unavailabilityAlertVerification(int fileId) {
        AvailabilityState currentStatusTiming = getCurrentStatusDuration(fileId);
        if (currentStatusTiming == null) {
            LOGGER.error("Unavailability Alert Verification currentStatus timing is null for fileId : " + fileId);
            return false;
        } else if (currentStatusTiming.getStatus().equals(AvailabilityStatus.AVAILABLE)) {
            return false;
        } else {
            Long duration = TimeUnit.MILLISECONDS.toMinutes(currentStatusTiming.getEndDate().getTime() - currentStatusTiming.getStartDate().getTime());
            if (duration > (availabilityTiming)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Check availabilities for a specific file and send mail alert if unavailable since more than @availabilityTiming minutes and no alert has been
     * already send
     *
     * @param fileId
     */
    public boolean triggerAlerting(int fileId) {
        DBFilesAvailability lastAvailabilityEntry = availabilityRepository.findTopByFileIdOrderByCheckDateDesc(fileId);
        if (lastAvailabilityEntry == null) {
            LOGGER.error("Is Already Alert - file availability null for file id : " + fileId);
        } else if (!lastAvailabilityEntry.getAlerted() && unavailabilityAlertVerification(fileId)) {
            alertingService.sendAvailabilityAlert(tlService.findByXmlFileId(fileId));
            // Update flag
            lastAvailabilityEntry.setAlerted(true);
            availabilityRepository.save(lastAvailabilityEntry);
            return true;
        }
        return false;
    }

}
