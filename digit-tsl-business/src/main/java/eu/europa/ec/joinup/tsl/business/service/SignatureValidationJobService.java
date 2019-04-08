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

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Validation job for signature
 */
@Service
public class SignatureValidationJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureValidationJobService.class);

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private SignersService signersService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private CountryService countryService;

    public void start() {
        LOGGER.debug("**** START ****");
        DBTrustedLists lotl = tlRepository.findByTerritoryAndStatusAndArchiveFalse(countryService.getLOTLCountry(), TLStatus.PROD);
        if (lotl != null) {
            Map<String, List<CertificateToken>> potentialSigners = signersService.getTLPotentialsSigners();

            List<DBTrustedLists> unarchivedTLs = tlRepository.findAllByArchiveFalseOrderByNameAsc();
            if (CollectionUtils.isNotEmpty(unarchivedTLs)) {
                for (DBTrustedLists unarchivedTL : unarchivedTLs) {
                    if (TLType.LOTL.equals(unarchivedTL.getType())) {
                        tlValidator.checkLOTL(unarchivedTL);
                    } else {
                        tlValidator.checkTL(unarchivedTL, potentialSigners.get(unarchivedTL.getTerritory().getCodeTerritory()));
                    }
                }
            }
        }
    }
}
