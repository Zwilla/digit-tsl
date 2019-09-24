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

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceDto;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceHistory;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBService;
import eu.europa.ec.joinup.tsl.model.DBTrustServiceProvider;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Trusted list data & Certificate loader
 */
@Service
public class TLDataLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLDataLoaderService.class);

    @Value("${lotl.territory}")
    private String lotlTerritory;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private TLDataService tlDataService;

    @Autowired
    private TLCertificateService tlCertificateService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    /**
     * Refresh all entries of service data and trusted list certificates with current production trusted list
     */
    @Async
    public void refreshAllTablesValue() {
        for (DBCountries country : countryService.getAll()) {
            if (country.getCodeTerritory().equals(lotlTerritory)) {
                // LOTL
                updateLOTLCertificates();
            } else {
                // TrustedList
                updateTrustedListData(country);
            }
        }
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, "SYSTEM", "RUN SERVICE DATA REFRESH - FINISH");
    }

    /**
     * Upadate the LOTL location in application properties
     */
    @Transactional(value = TxType.REQUIRED)
    public void updateLOTLLocation() {
        DBTrustedLists dbLOTL = tlService.getLOTL();
        if (dbLOTL == null) {
            LOGGER.error("Update LOTL certificates, trusted list is null");
        } else {
            LOGGER.debug("Update LOTL certificates for " + dbLOTL.getId() + "[ " + dbLOTL.getName() + "]");
            TL lotl = tlService.getPublishedTLByCountry(dbLOTL.getTerritory());
            if ((lotl != null) && !CollectionUtils.isEmpty(lotl.getPointers())) {
                for (TLPointersToOtherTSL pointer : lotl.getPointers()) {
                    if (pointer.getSchemeTerritory().equals(lotlTerritory) && pointer.getMimeType().equals(MimeType.XML)) {
                        applicationPropertyService.updateLOTLUrl(pointer.getTlLocation());
                        return;
                    }
                }
            }
        }
    }

    /**
     * Update DB_CERTIFICATES of the LOTL
     */
    @Transactional(value = TxType.REQUIRED)
    public void updateLOTLCertificates() {
        DBTrustedLists dbLOTL = tlService.getLOTL();
        if (dbLOTL == null) {
            LOGGER.error("Update LOTL certificates, trusted list is null");
        } else {
            LOGGER.debug("Update LOTL certificates for " + dbLOTL.getId() + "[ " + dbLOTL.getName() + "]");
            TL lotl = tlService.getPublishedTLByCountry(dbLOTL.getTerritory());
            if (lotl != null) {
                tlCertificateService.deleteLOTL();
                if (!CollectionUtils.isEmpty(lotl.getPointers())) {
                    for (TLPointersToOtherTSL pointer : lotl.getPointers()) {
                        if (!CollectionUtils.isEmpty(pointer.getServiceDigitalId()) && (pointer.getSchemeTerritory() != null) && pointer.getMimeType().equals(MimeType.XML)) {
                            for (TLDigitalIdentification digitalId : pointer.getServiceDigitalId()) {
                                if (!CollectionUtils.isEmpty(digitalId.getCertificateList())) {
                                    for (TLCertificate certificate : digitalId.getCertificateList()) {
                                        if (certificate.getToken() == null) {
                                            LOGGER.error("UpdateLOTLCertificates - CertificateToken NULL found for " + pointer.getSchemeTerritory());
                                        } else {
                                            tlCertificateService.addCertificateEntry(certificate.getToken(), pointer.getSchemeTerritory(), dbLOTL.getType(), null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Update DB_TRUST_SERVICE/DB_SERVICE/DB_CERTIFICATES tables by @DBCountries
     *
     * @param country
     */
    public void updateTrustedListData(DBCountries country) {
        try {
            loadTrustedListData(country);
        } catch (DataIntegrityViolationException e) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.DATABASE, AuditStatus.ERROR, country.getCodeTerritory(), 0, "SYSTEM", "Data integrity violation.");
            LOGGER.error("Update trusted list data, Data integrity violation : " + country.getCodeTerritory(), e);
        }
    }

    @Transactional(value = TxType.REQUIRES_NEW)
    public void loadTrustedListData(DBCountries country) {
        DBTrustedLists dbTl = tlService.getPublishedDbTLByCountry(country);
        if (dbTl == null) {
            LOGGER.error("Update trusted list data, DB trusted list is null for country code : " + country.getCodeTerritory());
        } else {
            TL tl = tlService.getPublishedTLByCountry(dbTl.getTerritory());
            if (tl == null) {
                LOGGER.error("Update trusted list data, TL DTO is null for country code : " + country.getCodeTerritory());
            } else {
                LOGGER.debug("Update trusted list data for " + dbTl.getId() + " [ " + dbTl.getName() + "]");
                tlDataService.deleteDataByCountry(country.getCodeTerritory());
                tlCertificateService.deleteByCountryCode(dbTl.getTerritory().getCodeTerritory(), TLType.TL);
                if (!CollectionUtils.isEmpty(tl.getServiceProviders())) {
                    for (TLServiceProvider tsp : tl.getServiceProviders()) {
                        // Persist Trust Service Provider
                        DBTrustServiceProvider dbTSP = tlDataService.addTSPEntry(tsp, country.getCodeTerritory(), tl.getSchemeInformation().getSequenceNumber());
                        if (!CollectionUtils.isEmpty(tsp.getTSPServices())) {
                            for (TLServiceDto service : tsp.getTSPServices()) {
                                // Persist Service
                                DBService dbService = tlDataService.addServiceEntry(service, dbTSP, country.getCodeTerritory());
                                dbTSP.getServices().add(dbService);
                                // Persist service certificate(s)
                                if (!CollectionUtils.isEmpty(service.getDigitalIdentification())) {
                                    for (TLDigitalIdentification digit : service.getDigitalIdentification()) {
                                        if (!CollectionUtils.isEmpty(digit.getCertificateList())) {
                                            for (TLCertificate certificate : digit.getCertificateList()) {
                                                // try {
                                                CertificateToken certificateToken = CertificateTokenUtils.loadCertificate(certificate.getCertEncoded());
                                                if (certificateToken == null) {
                                                    LOGGER.error("UpdateTrustedListData - CertificateToken NULL found for " + country.getCodeTerritory());
                                                } else {
                                                    tlCertificateService.addCertificateEntry(certificateToken, country.getCodeTerritory(), dbTl.getType(), dbService);
                                                }
                                            }
                                        }
                                    }
                                }

                                // Persist service history
                                if (!CollectionUtils.isEmpty(service.getHistory())) {
                                    for (TLServiceHistory history : service.getHistory()) {
                                        tlDataService.addHistoryEntry(history, dbService, country.getCodeTerritory());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
