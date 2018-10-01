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
package eu.europa.ec.joinup.tsl.business.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.constant.ServiceLegalType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticCountry;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionCriterias;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionType;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticGeneric;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticTSP;
import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticType;

public class StatisticCSVWriter {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticCSVWriter.class);

    private static final String NEW_LINE_SEPARATOR = "\n";

    public static <T extends StatisticGeneric> ByteArrayOutputStream generateStatsCSV(List<T> genericStats, StatisticExtractionCriterias criteria) {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFileFormat)) {

            //Dynamic header
            csvPrinter.printRecord(generateHeader(criteria));

            //Dynamic records
            for (StatisticGeneric statisticGeneric : genericStats) {
                csvPrinter.printRecord(extractRecord(statisticGeneric, criteria));
            }

            return baos;
        } catch (IOException e) {
            LOGGER.error("Error while writing CSV Country statistics", e);
            throw new IllegalStateException(bundle.getString("error.stats.generation"));
        }
    }

    /**
     * Generate dynamic header based on criteria
     *
     * @param criteria
     */
    private static Object[] generateHeader(StatisticExtractionCriterias criteria) {
        List<String> header = new ArrayList<>();

        //Country Code
        header.add(bundle.getString("tCC"));
        //SequenceNumber
        if (criteria.getShowSequenceNumber()) {
            header.add(bundle.getString("tlBrowser.sequenceNumber"));
        }
        if (criteria.getType().equals(StatisticExtractionType.COUNTRY)) {
            //TSP;QTSP;QTSP TOB
            header.add(bundle.getString("type.nb.tsp"));
            header.add(bundle.getString("type.nb.tsp.q.actif"));
            if (criteria.getShowTOB()) {
                header.add(bundle.getString("type.nb.tsp.q.tob"));
            }
        } else if (criteria.getType().equals(StatisticExtractionType.TSP)) {
            //Name
            header.add(bundle.getString("serviceProvider.name"));
            //TradeName
            if (criteria.getShowTradeName()) {
                header.add(bundle.getString("serviceProvider.tradeName"));
            }
        }

        //Legal types header
        for (ServiceLegalType serviceLegalType : ServiceLegalType.values()) {
            if (serviceLegalType.isQualified() && criteria.getShowQualified()) {
                header.addAll(generateLegalTypeHeader(serviceLegalType, criteria.getShowTOB()));
            } else if (!serviceLegalType.isQualified() && criteria.getShowUnqualified()) {
                header.addAll(generateLegalTypeHeader(serviceLegalType, criteria.getShowTOB()));
            }
        }

        return header.toArray(new String[header.size()]);
    }

    /**
     * Generate dynamic legal type header based on showTOB value
     *
     * @param serviceLegalType
     * @param showTOB
     */
    private static List<String> generateLegalTypeHeader(ServiceLegalType serviceLegalType, Boolean showTOB) {
        List<String> legalTypeHeader = new ArrayList<>();
        //Active
        legalTypeHeader.add(bundle.getString("type.active").replace("%TYPE%", serviceLegalType.getCode()));
        if (showTOB) {
            legalTypeHeader.add(bundle.getString("type.active.tob").replace("%TYPE%", serviceLegalType.getCode()));
        }

        //Inactive
        legalTypeHeader.add(bundle.getString("type.inactive").replace("%TYPE%", serviceLegalType.getCode()));
        if (showTOB) {
            legalTypeHeader.add(bundle.getString("type.inactive.tob").replace("%TYPE%", serviceLegalType.getCode()));
        }
        return legalTypeHeader;
    }

    private static <T extends StatisticGeneric> Object[] extractRecord(T statisticGeneric, StatisticExtractionCriterias criteria) {
        List<Object> record = new ArrayList<>();
        //Country Code
        record.add(statisticGeneric.getCountryCode());
        //Sequence Number
        if (criteria.getShowSequenceNumber()) {
            record.add(statisticGeneric.getSequenceNumber());
        }

        if (statisticGeneric instanceof StatisticCountry) {
            record.add(((StatisticCountry) statisticGeneric).getNbTSP());
            if (criteria.getShowTOB()) {
                //Add Active and Active TOB in two differents columns
                record.add(((StatisticCountry) statisticGeneric).getNbQActive());
                record.add(((StatisticCountry) statisticGeneric).getNbQTOB());
            } else {
                //Sum of Active and Active TOB in same column
                record.add(((StatisticCountry) statisticGeneric).getNbQActive() + ((StatisticCountry) statisticGeneric).getNbQTOB());
            }
        } else if (statisticGeneric instanceof StatisticTSP) {
            //Name
            record.add(((StatisticTSP) statisticGeneric).getName());
            //Trade Name
            if (criteria.getShowTradeName()) {
                record.add(((StatisticTSP) statisticGeneric).getTradeName());
            }
        }

        for (StatisticType statisticType : statisticGeneric.getTypes().values()) {
            if ((statisticType.getType().isQualified() && criteria.getShowQualified()) || (!statisticType.getType().isQualified() && criteria.getShowUnqualified())) {
                if (criteria.getShowTOB()) {
                    record.add(statisticType.getNbActive());
                    record.add(statisticType.getNbActiveTOB());
                    record.add(statisticType.getNbInactive());
                    record.add(statisticType.getNbInactiveTOB());
                } else {
                    record.add(statisticType.getAllActive());
                    record.add(statisticType.getAllInactive());
                }
            }
        }
        return record.toArray(new Object[record.size()]);
    }

}
