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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.FileService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.util.TLDigitalIdentityUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class NotificationValidator {

    @Value("${notification.shift.period}")
    private int notificationShiftPeriod;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationValidator.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private FileService fileService;

    @Autowired
    private DigitalIdentityValidator digitalIdentityValidator;

    @Autowired
    private CountryService countryService;

    public List<CheckResultDTO> validateNotification(List<CheckDTO> checks, TLPointersToOtherTSL pointer) {
        List<CheckResultDTO> results = new ArrayList<>();
        for (CheckDTO check : checks) {
            switch (check.getName()) {
            case IS_SIGNING_CERTIFICATE_PRESENT:
                isSigningCertificatePresent(check, pointer, results);
                break;
            case AT_LEAST_TWO_CERT_NOT_EXPIRED:
                atLeastTwoCertificatesNotExpired(check, pointer, results);
                break;
            case NEW_OLD_CERT_TIME_GAP:
                calculNewerOlderCertDateValidity(check, pointer, results);
                break;
            default:
                LOGGER.warn("Unsupported check " + check.getName());
                break;
            }
        }
        return results;
    }

    /**
     * Verify that time gap between older and newer cert(not expired) is less than 3 months
     *
     * @param check
     * @param pointer
     * @param results
     */
    private void calculNewerOlderCertDateValidity(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        Calendar calV = Calendar.getInstance();
        calV.setTime(new Date());
        calV.add(Calendar.DAY_OF_MONTH, notificationShiftPeriod);
        Date verificationDay = calV.getTime();
        if (!digitalIdentityValidator.isShiftedPeriodValid(TLDigitalIdentityUtils.retrieveCertificates(pointer.getServiceDigitalId()), verificationDay)) {
            results.add(new CheckResultDTO(Tag.NOTIFICATION.toString() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, check, false));
        }
    }

    /**
     * Verify that there is at least two certificates not expired
     *
     * @param check
     * @param pointer
     * @param results
     */
    private void atLeastTwoCertificatesNotExpired(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        if (!digitalIdentityValidator.isTwoCertificatesNotExpired(TLDigitalIdentityUtils.retrieveCertificates(pointer.getServiceDigitalId()), new Date())) {
            results.add(new CheckResultDTO(Tag.NOTIFICATION.toString() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, check, false));
        }
    }

    /**
     * Verify is there is the trusted list signing certificate is still present in notification
     *
     * @param check
     * @param pointer
     * @param results
     */
    private void isSigningCertificatePresent(CheckDTO check, TLPointersToOtherTSL pointer, List<CheckResultDTO> results) {
        if (!CollectionUtils.isEmpty(pointer.getServiceDigitalId())) {
            for (TLDigitalIdentification digital : pointer.getServiceDigitalId()) {
                if ((digital != null) && (digital.getCertificateList() != null)) {
                    DBTrustedLists tlProd = tlService.getPublishedDbTLByCountry(countryService.getCountryByTerritory(pointer.getSchemeTerritory()));
                    if (tlProd != null) {
                        DBFiles dbF = tlProd.getXmlFile();
                        if (dbF.getLocalPath() == null) {
                            LOGGER.error("isSigningCertificatePresent - TL " + pointer.getSchemeTerritory() + " doesn't found. DB Files null. Check can be verified");
                            return;
                        }
                        File xml = fileService.getTSLFile(dbF);
                        CertificateToken signingCertificate = TLUtils.getSigningCertificate(xml);
                        if (signingCertificate != null) {
                            for (TLCertificate cert : digital.getCertificateList()) {
                                if (cert.getToken() == null) {
                                    cert.setTokenFromEncoded();
                                }
                                if (Utils.toBase64(signingCertificate.getEncoded()).equals(Utils.toBase64(cert.getToken().getEncoded()))) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        results.add(new CheckResultDTO(Tag.NOTIFICATION.toString() + "_" + Tag.SERVICE_DIGITAL_IDENTITY, check, false));
    }

}
