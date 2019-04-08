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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.util.TLDigitalIdentityUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class DigitalIdentityValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalIdentityValidator.class);

    public boolean isBase64Certificate(byte[] certificateBinaries) {
        try {
            CertificateToken certificate = DSSUtils.loadCertificate(certificateBinaries);
            if (certificate != null) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("Certificate " + Base64.encodeBase64String(certificateBinaries) + " is not correct");
        }
        return false;
    }

    public boolean isCorrectX509SKI(byte[] ski, CertificateToken certificate) {
        try {
            byte[] expectedSki = TLUtils.getSki(certificate);
            if (!Objects.deepEquals(expectedSki, ski)) {
                LOGGER.debug("Wrong X509SKI detected " + Base64.encodeBase64String(ski) + " should be " + Base64.encodeBase64String(expectedSki));
            } else {
                return true;
            }
        } catch (Exception e) {
            LOGGER.debug("Unable to compute SKI for Certificate " + Utils.toBase64(certificate.getEncoded()));
        }
        return false;
    }

    public boolean isCorrectX509SubjectName(String subjectName, CertificateToken certificate) {
        X500Principal x500Principal = DSSUtils.getX500PrincipalOrNull(subjectName);
        if (!DSSUtils.x500PrincipalAreEquals(certificate.getSubjectX500Principal(), x500Principal)) {
            if (!certificate.getSubjectX500Principal().toString().equalsIgnoreCase(subjectName)) {
                LOGGER.debug("Wrong SubjectName detected " + x500Principal + " should be " + certificate.getSubjectX500Principal());
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean isNotExpired(TLCertificate certificate) {
        Date currentDate = new Date();
        if (certificate.getCertAfter().before(currentDate)) {
            return false;
        }
        return true;
    }

    /**
     * Loop through TLDigitalIdentification and return 'true' if there is at least two certificates not expired
     *
     * @param serviceDigitalId
     */
    public boolean isTwoCertificatesNotExpired(List<TLCertificate> certificates, Date checkDate) {
        int nbCertOK = 0;
        for (TLCertificate certificate : certificates) {
            if (certificate.getCertNotBefore().before(checkDate) && certificate.getCertAfter().after(checkDate)) {
                nbCertOK = nbCertOK + 1;
            }
        }
        return (nbCertOK >= 2);
    }

    /**
     * Verify that there at least 3 months between the nearest and the furthest expiration date of the current certificate chain
     *
     * @param serviceDigitalId
     */
    public boolean isShiftedPeriodValid(List<TLCertificate> certificates, Date verificationDay) {
        Boolean isValid = false;

        // Get certificates not expired
        List<TLCertificate> notExpiredCertificates = new ArrayList<>();
        for (TLCertificate certificate : certificates) {
            if (certificate.getCertAfter().after(verificationDay)) {
                notExpiredCertificates.add(certificate);
            }
        }
        // Sort by expiration date
        TLDigitalIdentityUtils.sortByStartDate(notExpiredCertificates);
        if (CollectionUtils.isEmpty(notExpiredCertificates)) {
            return isValid;
        }

        // Loop through certificates and retrieve a certificate chain without interruption
        Set<TLCertificate> certificateChainSet = new HashSet<>();
        for (TLCertificate certificate : notExpiredCertificates) {
            for (TLCertificate certificateCompare : notExpiredCertificates) {
                if (certificate.getCertAfter().after(certificateCompare.getCertNotBefore())) {
                    certificateChainSet.add(certificate);
                }
            }
        }

        // Order certificate chain by expiration date
        List<TLCertificate> certificateChainList = new ArrayList<>(certificateChainSet);
        if (!CollectionUtils.isEmpty(certificateChainList) && (certificateChainList.size() >= 2)) {
            TLDigitalIdentityUtils.sortByExpirationDate(certificateChainList);

            // Add 3 months to the furthest expiration date
            Calendar cal = Calendar.getInstance();
            cal.setTime(certificateChainList.get(0).getCertAfter());
            cal.add(Calendar.MONTH, 3);
            Date endOfShiftedPeriod = cal.getTime();

            if (endOfShiftedPeriod.before(certificateChainList.get(certificateChainList.size() - 1).getCertAfter())) {
                isValid = true;
            }
        }
        return isValid;

    }
}
