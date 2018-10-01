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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.BreakElement;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.BreakType;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.CertificateBreakElement;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.CertificateElement;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Manage trusted list break events (notified certificates/next update date expiration).
 */
@Service
public class TLBreakValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLBreakValidationService.class);

    @Value("${next.update.date.break.day}")
    private String nextUpdateDateBreakDayProperty;

    @Value("${certificate.break.day}")
    private String certificateBreakDayProperty;

    @Autowired
    private TLCertificateService certificateService;

    @Autowired
    private FileService fileService;

    /**
     * Initialize trusted list break status informations
     *
     * @param dbTL
     * @param checkDate
     */
    public TLBreakStatus initTLBreakStatus(DBTrustedLists dbTL, Date checkDate) {
        //Init certificates break days
        List<Integer> certificateBreakDays = propertyToIntegerList(certificateBreakDayProperty);

        //Init Status
        TLBreakStatus tlStatus = new TLBreakStatus(dbTL, checkDate, certificateBreakDays,
                certificateService.getByCountryCode(dbTL.getTerritory().getCodeTerritory(), TLType.LOTL, checkDate));

        //Verify element
        tlStatus.setNextUpdateDateElement(verifyNextUpdateDateBreakStatus(dbTL, checkDate));
        tlStatus.setSigningCertificateElement(verifySigningCertificateBreakStatus(tlStatus));
        tlStatus.setTwoCertificatesElement(verifyTwoCertificatesElement(tlStatus));
        tlStatus.setShiftedPeriodElement(verifyShiftedPeriodBreakStatus(tlStatus));

        //Set alert status
        tlStatus.setBreakType(determineBreakStatus(tlStatus));
        return tlStatus;
    }

    /* ----- ----- Verify Element ----- ----- */

    /**
     * Check next update date expiration
     *
     * @param dbTL
     * @param checkDate
     */
    private BreakElement verifyNextUpdateDateBreakStatus(DBTrustedLists dbTL, Date checkDate) {
        BreakElement nextUpdateDateElement = new BreakElement();
        if (dbTL.getNextUpdateDate() != null) {
            nextUpdateDateElement.verify(dbTL.getNextUpdateDate(), checkDate, propertyToIntegerList(nextUpdateDateBreakDayProperty));
        } else {
            LOGGER.error("Next update date is null for dbTL id : " + dbTL.getId());
        }
        return nextUpdateDateElement;
    }

    /**
     * Check signing certificate expiration date
     *
     * @param tlStatus
     */
    private CertificateBreakElement verifySigningCertificateBreakStatus(TLBreakStatus tlStatus) {
        CertificateBreakElement signingCertificateDateElement = new CertificateBreakElement();
        if (tlStatus.getTl().getXmlFile() != null) {
            File xmlFile = fileService.getTSLFile(tlStatus.getTl().getXmlFile());
            CertificateToken signingCertificate = TLUtils.getSigningCertificate(xmlFile);
            if (signingCertificate == null) {
                LOGGER.error("Signing certificate is is null for dbTL id : " + tlStatus.getTl().getId());
            } else {
                String signingCertificateB64 = Utils.toBase64(signingCertificate.getEncoded());
                Boolean certificateFind = false;
                for (CertificateElement certificate : tlStatus.getCertificates()) {
                    if (signingCertificateB64.equals(certificate.getBase64())) {
                        certificateFind = true;
                        signingCertificateDateElement.setBreakDay(certificate.isBreakDay());
                        signingCertificateDateElement.setExpireIn(certificate.getExpirationIn());
                        signingCertificateDateElement.getCertificatesAffected().add(certificate);
                        if (tlStatus.getCertificateLimitMax() > certificate.getExpirationIn()) {
                            signingCertificateDateElement.setAlert(true);
                        } else {
                            signingCertificateDateElement.setAlert(false);
                        }
                    }
                }

                //Signing certificate is not active or not notified in the LOTL
                if (!certificateFind) {
                    return initUnverifiedCertificateElement();
                }
            }
        } else {
            LOGGER.error("XML File is is null for dbTL id : " + tlStatus.getTl().getId());
        }
        return signingCertificateDateElement;
    }

    /**
     * Check certificates to find the one that break two certificates requirement
     * NOTE: If there is only one certificate, alert set 'true' , expireIn set to 0 and certificatesAffected null
     *
     * @param tlStatus
     */
    private CertificateBreakElement verifyTwoCertificatesElement(TLBreakStatus tlStatus) {
        if (!CollectionUtils.isEmpty(tlStatus.getCertificates()) || (tlStatus.getCertificates().size() >= 2)) {
            //Get certificates active and sort from nearest to farthest expiration date
            List<CertificateElement> certificatesActive = tlStatus.retrieveCurrentCertificates();
            if (certificatesActive.size() < 2) {
                return initUnverifiedCertificateElement();
            } else {
                certificateService.sortCertificateByExpiration(certificatesActive);
                //Loop through certificates and determine if a certificate break the requirement
                //NOTE: if requirement has break more than once, retrieve the nearest break
                for (CertificateElement certificateAct : certificatesActive) {
                    List<CertificateElement> certificatesUpToDate = certificateService.getByCountryCodeBeforeDate(tlStatus.getTl().getTerritory().getCodeTerritory(),
                            certificateAct.getNotAfter(), TLType.LOTL);

                    //Check validity period
                    if (!isTwoCertificatesValid(certificatesUpToDate)) {
                        return initCertificateBreakElement(tlStatus, certificateAct);
                    }

                }
                return new CertificateBreakElement();
            }
        }
        //Requirement never verified
        return initUnverifiedCertificateElement();
    }

    /**
     * Check certificates to find the one that break shifted validity period requirement
     * NOTE: If there is only one certificate, alert set 'true' , expireIn set to 0 and certificatesAffected null
     *
     * @param tlStatus
     */
    private CertificateBreakElement verifyShiftedPeriodBreakStatus(TLBreakStatus tlStatus) {
        CertificateElement breakElement = null;

        if (!CollectionUtils.isEmpty(tlStatus.getCertificates()) || (tlStatus.getCertificates().size() >= 2)) {
            //Get certificates active and sort from nearest to farthest expiration date
            List<CertificateElement> certificatesActive = tlStatus.getCertificatesNotBeforePast();
            certificateService.sortCertificateByExpiration(certificatesActive);
            certificatesActive = Lists.reverse(certificatesActive);

            //Loop through certificates and determine which certificate break the requirement
            //NOTE: if requirement has break more than once, retrieve the nearest break
            for (CertificateElement certificateLR : certificatesActive) {
                //Get active certificate chain when certificateLR will expire
                List<CertificateElement> certificateChain = certificateService.getCertificateChain(new CertificateElement(certificateLR),
                        new ArrayList<>(tlStatus.getCertificates()));

                //Check validity period
                if (isShiftedValidityPeriodValid(certificateChain)) {
                    if (breakElement == null) {
                        //Condition is currently valid
                        return initCertificateBreakElement(tlStatus, certificateLR);
                    } else {
                        return initCertificateBreakElement(tlStatus, breakElement);
                    }
                } else {
                    //Update break element last occurence
                    breakElement = certificateLR;
                }
            }
        }

        if (breakElement != null) {
            //Verify condition with the last certificate one day before expiration
            List<CertificateElement> certificateChain = certificateService.getCertificateChain(new CertificateElement(breakElement), new ArrayList<>(tlStatus.getCertificates()));
            certificateChain.add(breakElement);

            if (isShiftedValidityPeriodValid(certificateChain)) {
                //Requirement has been correct once
                return initCertificateBreakElement(tlStatus, breakElement);
            }
        }

        //Requirement never verified
        return initUnverifiedCertificateElement();
    }

    /* ----- ----- Utils Method ----- ----- */

    /**
     * Verify is there is 2 certificate valid and not expired in @certificatesUpToDate list
     *
     * @param certificatesUpToDate
     */
    private boolean isTwoCertificatesValid(List<CertificateElement> certificatesUpToDate) {
        int nbCertNotExpired = 0;
        for (CertificateElement certificateCopy : certificatesUpToDate) {
            if (!certificateCopy.isExpired()) {
                nbCertNotExpired = nbCertNotExpired + 1;
            }
        }

        return (nbCertNotExpired >= 2);
    }

    /**
     * Verify if shifted validity period of 3 months is respected between furthest and nearest expiration date of @certificates
     *
     * @param certificateChain
     */
    private boolean isShiftedValidityPeriodValid(List<CertificateElement> certificateChain) {
        if (!CollectionUtils.isEmpty(certificateChain) && (certificateChain.size() >= 2)) {
            certificateService.sortCertificateByExpiration(certificateChain);

            //Add 3 months to the furthest expiration date
            Calendar cal = Calendar.getInstance();
            cal.setTime(certificateChain.get(0).getNotAfter());
            cal.add(Calendar.MONTH, 3);
            Date endOfShiftedPeriod = cal.getTime();

            return (endOfShiftedPeriod.before(certificateChain.get(certificateChain.size() - 1).getNotAfter()));
        }
        return false;
    }

    /**
     * Determine trusted list break status
     */
    private BreakType determineBreakStatus(TLBreakStatus tlStatus) {
        //Certificate trigger
        for (CertificateElement certificate : tlStatus.getCertificates()) {
            if (certificate.isBreakDay()) {
                return BreakType.DAY_OF_BREAK;
            }
        }
        //Next update date trigger
        if (tlStatus.getNextUpdateDateElement().isBreakDay()) {
            return BreakType.DAY_OF_BREAK;
        }

        //'Alert' only
        if (tlStatus.getNextUpdateDateElement().isAlert() || tlStatus.getSigningCertificateElement().isAlert() || tlStatus.getTwoCertificatesElement().isAlert()
                || tlStatus.getShiftedPeriodElement().isAlert()) {
            return BreakType.WARNING;
        } else {
            return BreakType.NONE;
        }
    }

    /**
     * Init certificate element with requirementVerifierCertificate
     *
     * @param tlStatus
     * @param limit
     * @param certificateBreakElement
     * @param requirementVerifierCertificate
     */
    private CertificateBreakElement initCertificateBreakElement(TLBreakStatus tlStatus, CertificateElement requirementVerifierCertificate) {
        CertificateBreakElement certificateBreakElement = new CertificateBreakElement();
        //Init element
        certificateBreakElement.setBreakDay(requirementVerifierCertificate.isBreakDay());
        certificateBreakElement.setExpireIn(requirementVerifierCertificate.getExpirationIn());
        if (tlStatus.getCertificateLimitMax() > requirementVerifierCertificate.getExpirationIn()) {
            certificateBreakElement.setAlert(true);
        } else {
            certificateBreakElement.setAlert(false);
        }
        //Set certificates impacted
        //NOTE: required when two certificates expire the same day
        certificateBreakElement.setCertificatesAffected(certificateService.getExpiredCertificateByCountryCode(tlStatus.getTl().getTerritory().getCodeTerritory(),
                requirementVerifierCertificate.getNotAfter(), TLType.LOTL, tlStatus.getCheckDate()));
        return certificateBreakElement;
    }

    /**
     * Init certificate element when requirement can't be verified
     *
     * @param certificateBreakElement
     */
    private CertificateBreakElement initUnverifiedCertificateElement() {
        CertificateBreakElement certificateBreakElement = new CertificateBreakElement();
        certificateBreakElement.setVerifiable(false);
        certificateBreakElement.setAlert(true);
        certificateBreakElement.setBreakDay(false);
        certificateBreakElement.setCertificatesAffected(null);
        certificateBreakElement.setExpireIn(0);
        return certificateBreakElement;
    }

    /**
     * Transform string property to integer list
     *
     * @param property
     */
    private List<Integer> propertyToIntegerList(String property) {
        List<Integer> breakTimes = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(property) && property.contains(";")) {
                String[] sProperty = property.split(";");
                for (String sP : sProperty) {
                    breakTimes.add(Integer.parseInt(sP));
                }
            }
        } catch (NumberFormatException e) {
            breakTimes = new ArrayList<>();
            breakTimes.add(0);
            LOGGER.error("Break property to integer exception on property : " + property, e);
        }
        return breakTimes;
    }

}
