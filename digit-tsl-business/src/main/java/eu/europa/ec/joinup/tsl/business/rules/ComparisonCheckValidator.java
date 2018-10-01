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
package eu.europa.ec.joinup.tsl.business.rules;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.util.LocationUtils;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
public class ComparisonCheckValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparisonCheckValidator.class);

    public Set<CheckResultDTO> validate(CheckDTO check, TL previousVersion, TL currentVersion) {
        Set<CheckResultDTO> results = new HashSet<>();
        switch (check.getName()) {
            case IS_SEQUENCE_NUMBER_INCREMENTED:
                runCheckOnSequenceNumber(check, previousVersion, currentVersion, results);
                break;
            case IS_ALL_PREVIOUS_TSP_SERVICE_PROVIDERS_PRESENT:
                runCheckOnTspServiceProviders(check, previousVersion, currentVersion, results);
                break;
            case IS_ALL_PREVIOUS_TSP_SERVICES_PRESENT:
                runCheckOnTspServices(check, previousVersion, currentVersion, results);
                break;
            case IS_ALL_PREVIOUS_TSP_SERVICE_HISTORIES_PRESENT:
                runCheckOnTspServiceHistories(check, previousVersion, currentVersion, results);
                break;
            default:
                LOGGER.warn("Unsupported " + check.getName());
        }
        for (CheckResultDTO result : results) {
            result.setLocation(LocationUtils.idUserReadable(currentVersion, result.getId()));
        }
        return results;
    }

    private void runCheckOnSequenceNumber(CheckDTO check, TL previousVersion, TL currentVersion, Set<CheckResultDTO> results) {
        TLSchemeInformation currentSI = currentVersion.getSchemeInformation();
        TLSchemeInformation previousSI = previousVersion.getSchemeInformation();
        if ((currentSI != null) && (previousSI != null)) {
            this.runCheckOnSequenceNumber(check, currentSI.getId(), previousSI.getSequenceNumber(), currentSI.getSequenceNumber(), results);
        }
    }

    public void runCheckOnSequenceNumber(CheckDTO check, String schemeInfoId, int previousSN, int currentSN, Set<CheckResultDTO> results) {
        results.add(new CheckResultDTO(schemeInfoId + "_" + Tag.SEQUENCE_NUMBER, check, (currentSN > previousSN)));
    }

    private void runCheckOnTspServiceProviders(CheckDTO check, TL previousVersion, TL currentVersion, Set<CheckResultDTO> results) {
        List<TLServiceProvider> currentServiceProviders = currentVersion.getServiceProviders();
        List<TLServiceProvider> previousServiceProviders = previousVersion.getServiceProviders();
        if (CollectionUtils.isNotEmpty(previousServiceProviders)) {
            results.add(new CheckResultDTO(currentVersion.getId() + "_" + Tag.TSP_SERVICE_PROVIDER, check,
                    CollectionUtils.size(currentServiceProviders) >= CollectionUtils.size(previousServiceProviders)));
        }
    }

    private void runCheckOnTspServices(CheckDTO check, TL previousVersion, TL currentVersion, Set<CheckResultDTO> results) {
        int nbServicesCurrentVersion = countServices(currentVersion);
        int nbServicesPreviousVersion = countServices(previousVersion);
        results.add(new CheckResultDTO(currentVersion.getId() + "_" + Tag.TSP_SERVICE_PROVIDER, check, nbServicesCurrentVersion >= nbServicesPreviousVersion));
    }

    private void runCheckOnTspServiceHistories(CheckDTO check, TL previousVersion, TL currentVersion, Set<CheckResultDTO> results) {
        int nbHistoriesCurrentVersion = countHistories(currentVersion);
        int nbHistoriesPreviousVersion = countHistories(previousVersion);
        results.add(new CheckResultDTO(currentVersion.getId() + "_" + Tag.TSP_SERVICE_PROVIDER, check, nbHistoriesCurrentVersion >= nbHistoriesPreviousVersion));
    }

    private int countServices(TL tl) {
        int nb = 0;
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                if (CollectionUtils.isNotEmpty(serviceProvider.getTSPServices())) {
                    nb += CollectionUtils.size(serviceProvider.getTSPServices());
                }
            }
        }
        return nb;
    }

    private int countHistories(TL tl) {
        int nb = 0;
        List<TLServiceProvider> serviceProviders = tl.getServiceProviders();
        if (CollectionUtils.isNotEmpty(serviceProviders)) {
            for (TLServiceProvider serviceProvider : serviceProviders) {
                List<TLServiceDto> services = serviceProvider.getTSPServices();
                if (CollectionUtils.isNotEmpty(services)) {
                    for (TLServiceDto service : services) {
                        if (CollectionUtils.isNotEmpty(service.getHistory())) {
                            nb += CollectionUtils.size(service.getHistory());
                        }
                    }
                }
            }
        }
        return nb;
    }

}
