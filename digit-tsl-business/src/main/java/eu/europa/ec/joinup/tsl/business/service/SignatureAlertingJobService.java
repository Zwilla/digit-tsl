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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;

/**
 * Alerting job for signature status
 */
@Service
public class SignatureAlertingJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureAlertingJobService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AbstractAlertingService alertingService;

    /**
     * Trigger signature alert job
     */
    public void start() {
        LOGGER.debug("**** START SIGNATURE ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.SIGNATURE_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start signature alerting job");
        for (DBCountries country : countryService.getAll()) {
            signatureAlert(country);
        }
        LOGGER.debug("**** END SIGNATURE ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.SIGNATURE_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "End signature alerting job");
    }

    /**
     * Get DB trusted list by country & send alert mail if signature is invalid
     *
     * @param country
     */
    public void signatureAlert(DBCountries country) {
        DBTrustedLists dbTL = tlService.getPublishedDbTLByCountry(country);
        if (dbTL == null) {
            LOGGER.error("SIGNATURE ALERT - Trusted list null for country : " + country.getCodeTerritory());
        } else {
            TLSignature tmpSignature = tlService.getSignatureInfo(dbTL.getId());
            if ((tmpSignature == null) || (tmpSignature.getIndication() == null)) {
                LOGGER.error("SIGNATURE ALERT - Signature null for trusted list " + dbTL.getId());
            } else if (!tmpSignature.getIndication().equals(SignatureStatus.VALID)) {
                alertingService.sendSignatureAlert(dbTL, tmpSignature);
            }
        }
    }
}
