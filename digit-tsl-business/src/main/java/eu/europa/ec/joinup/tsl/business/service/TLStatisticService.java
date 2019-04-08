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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticCountry;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionCriterias;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticGeneric;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticTSP;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticType;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.ServiceHistoryAbstractDataDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.tl.TSPDataDTO;
import eu.europa.ec.joinup.tsl.business.util.StatisticCSVWriter;

@Service
public class TLStatisticService {

    @Autowired
    private CountryService countryService;

    @Autowired
    private TLDataService dataService;

    @Autowired
    private TLService tlService;

    @Value("${lotl.territory}")
    private String lotlTerritory;

    private static final List<String> activeStatus = Arrays.asList("http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/granted",
            "http://uri.etsi.org/TrstSvc/TrustedList/Svcstatus/recognisedatnationallevel");

    /* ----- ----- CSV Generation ----- ----- */

    public StatisticExtractionCriterias initCriteria() {
        return new StatisticExtractionCriterias(countryService.getAllCountryCode());
    }

    /**
     * Generate generic stats as CSV
     *
     * @param genericStats
     * @param criteria
     */
    public ByteArrayOutputStream generateCSV(StatisticExtractionCriterias criteria) {
        List<StatisticGeneric> genericStats = new ArrayList<>();
        if (criteria.getType().equals(StatisticExtractionType.COUNTRY)) {
            if (StringUtils.isEmpty(criteria.getSpecificCountry())) {
                genericStats.addAll(getAllStatisticByCountry(criteria.getExtractDate()));
            } else {
                genericStats.add(getCountryStatisticByCountry(criteria.getSpecificCountry(), criteria.getExtractDate()));
            }
        } else {
            if (StringUtils.isEmpty(criteria.getSpecificCountry())) {
                genericStats.addAll(getAllStatisticByTSP(criteria.getExtractDate()));
            } else {
                genericStats.addAll(getCountryStatisticByTSP(criteria.getSpecificCountry(), criteria.getExtractDate()));
            }
        }
        return StatisticCSVWriter.generateStatsCSV(genericStats, criteria);
    }

    /* ----- ----- Search group by COUNTRY ----- ----- */

    /**
     * Get TL statistic for <b>ALL COUNTRIES</b> group by <b>COUNTRY</b>
     *
     * @param extractDate
     */
    public List<StatisticCountry> getAllStatisticByCountry(Date extractDate) {
        List<StatisticCountry> statistics = new ArrayList<>();
        for (String countryCode : countryService.getAllCountryCode()) {
            if (!countryCode.equals(lotlTerritory)) {
                statistics.add(getCountryStatisticByCountry(countryCode, extractDate));
            }
        }
        return statistics;
    }

    /**
     * Get TL statistic for a given <b>COUNTRY</b> group by <b>COUNTRY</b>
     *
     * @param countryCode
     * @param extractDate
     */
    public StatisticCountry getCountryStatisticByCountry(String countryCode, Date extractDate) {
        // Init result
        StatisticCountry countryStat = new StatisticCountry(countryCode, tlService.getProdSequenceNumberByTerritory(countryCode), extractDate);

        List<StatisticTSP> tspStatList = getCountryStatisticByTSP(countryCode, extractDate);

        // Loop through TSP entry
        for (StatisticTSP tspStat : tspStatList) {
            // At least one service
            if (tspStat.getNbService() > 0) {
                countryStat.incrementNbTSP();
            }
            // Init counter & params
            int nbActive = 0;
            int nbActiveTOB = 0;
            boolean isQualified = false;
            boolean isTOB = false;

            // Loop through TSP types
            for (StatisticType statisticType : tspStat.getTypes().values()) {
                if (statisticType.hasService() && statisticType.getType().isQualified()) {
                    // At least one service is qualified
                    isQualified = true;
                }

                // Increment StatisticType counter
                if (statisticType.getNbActive() > 0) {
                    nbActive = nbActive + 1;
                    countryStat.getTypes().get(statisticType.getType()).incrementCounter(true, false);

                } else if (statisticType.getNbActiveTOB() > 0) {
                    nbActiveTOB = nbActiveTOB + 1;
                    countryStat.getTypes().get(statisticType.getType()).incrementCounter(true, true);

                } else if (statisticType.getNbInactive() > 0) {
                    countryStat.getTypes().get(statisticType.getType()).incrementCounter(false, false);

                } else if (statisticType.getNbInactiveTOB() > 0) {
                    countryStat.getTypes().get(statisticType.getType()).incrementCounter(false, true);
                }

            }

            // All the active services are TOB
            if ((nbActive == 0) && (nbActiveTOB > 0)) {
                isTOB = true;
            }

            // At least one service is active and qualified
            if (((nbActive + nbActiveTOB) > 0) && isQualified) {
                if (isTOB) {
                    countryStat.incrementQTOB();
                } else {
                    countryStat.incrementQActive();
                }
            }

        }
        return countryStat;
    }

    /* ----- ----- Search group by TSP ----- ----- */

    /**
     * Get TL static for <b>ALL COUNTRIES</b> group by <b>TSP</b>
     *
     * @param extractDate
     */
    public List<StatisticTSP> getAllStatisticByTSP(Date extractDate) {
        // Init result
        List<StatisticTSP> tspStatList = new ArrayList<>();

        // Loop through all countries
        for (String countryCode : countryService.getAllCountryCode()) {
            dataService.findAllByCountry(countryCode);
            tspStatList.addAll(getCountryStatisticByTSP(countryCode, extractDate));
        }
        return sortStatisticTSP(tspStatList);
    }

    /**
     * Get TL static a given <b>COUNTRY</b> group by <b>TSP</b>
     *
     * @param countryCode
     * @param extractDate
     */
    public List<StatisticTSP> getCountryStatisticByTSP(String countryCode, Date extractDate) {
        // Init result
        List<StatisticTSP> tspStatList = new ArrayList<>();

        // Loop through TSP list
        for (TSPDataDTO tsp : dataService.findAllByCountry(countryCode)) {
            tspStatList.add(getTSPStatistic(tsp, extractDate));
        }

        return sortStatisticTSP(tspStatList);
    }

    /* ----- ----- Private methods ----- ----- */

    /**
     * Get TL statistic by <b>TSP</b> for <b>A GIVEN TSP</b>
     *
     * @param tsp
     * @param extractDate
     */
    private StatisticTSP getTSPStatistic(TSPDataDTO tsp, Date extractDate) {
        // Init result
        StatisticTSP statTSP = new StatisticTSP(tsp.getCountryCode(), tsp.getSequenceNumber(), tsp.getMName(), tsp.getMTradeName(), extractDate);

        if (!CollectionUtils.isEmpty(tsp.getServices())) {

            // Loop through Service list
            for (ServiceDataDTO service : tsp.getServices()) {
                ServiceHistoryAbstractDataDTO shAbstract = null;
                if (service.getStartingDate().before(extractDate)) {
                    // Current service already started before extract date
                    shAbstract = service;
                } else if (!CollectionUtils.isEmpty(service.getHistory())) {
                    // Current service not started before extract date. Loop through historic required
                    Boolean historyFound = false;
                    int index = 0;
                    while (!historyFound && (index < service.getHistory().size())) {
                        if (service.getHistory().get(index).getStartingDate().before(extractDate)) {
                            historyFound = true;
                            shAbstract = service.getHistory().get(index);
                        }
                        index = index + 1;
                    }
                }

                if (shAbstract == null) {
                    // No service or history found
                } else {
                    // Increment Nb of existing service
                    statTSP.incrementNbService();

                    // Verify is service is active
                    Boolean isActive = activeStatus.contains(shAbstract.getStatus());

                    // Verify if service is TakenOverBy
                    Boolean isTOB = !StringUtils.isEmpty(shAbstract.getTakenOverBy());

                    // Calcul legal types
                    for (String qType : shAbstract.getQTypes()) {
                        ServiceLegalType legalType = ServiceLegalType.getFromCode(qType);
                        statTSP.incrementType(legalType, isActive, isTOB);
                    }
                }

            }
        }
        return statTSP;
    }

    /* ----- ----- Sort ----- ----- */

    public List<StatisticTSP> sortStatisticTSP(List<StatisticTSP> tspList) {
        Collections.sort(tspList, new Comparator<StatisticTSP>() {
            @Override
            public int compare(StatisticTSP o1, StatisticTSP o2) {
                if (o1.getCountryCode().equals(o2.getCountryCode())) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o1.getCountryCode().compareTo(o2.getCountryCode());
                }
            }
        });
        return tspList;
    }
}
