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
package eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.model.DBTrustedLists;

/**
 * Trusted list break status based on next update date & signing certificate elements
 */
public class TLBreakStatus {

    private DBTrustedLists tl;

    private Date checkDate;

    private List<Integer> certificateLimits;

    private List<CertificateElement> certificates;

    private BreakType breakType;

    private BreakElement nextUpdateDateElement;

    private CertificateBreakElement signingCertificateElement;

    private CertificateBreakElement twoCertificatesElement;

    private CertificateBreakElement shiftedPeriodElement;

    public TLBreakStatus(DBTrustedLists dbTL, Date checkDate, List<Integer> certificateLimits, List<CertificateElement> certificatesList) {
        tl = dbTL;
        this.certificateLimits = certificateLimits;
        this.checkDate = checkDate;
        certificates = new ArrayList<>();
        int index = 1;
        for (CertificateElement certificate : certificatesList) {
            if (!certificate.isExpired() && certificateLimits.contains(certificate.getExpirationIn())) {
                certificate.setBreakDay(true);
            }

            certificate.setIndex(index);
            index = index + 1;
            certificates.add(certificate);

        }
        breakType = BreakType.NONE;
        nextUpdateDateElement = new BreakElement();
        signingCertificateElement = new CertificateBreakElement();
        twoCertificatesElement = new CertificateBreakElement();
        shiftedPeriodElement = new CertificateBreakElement();
    }

    // ----- ----- Get certificates ----- ----- //

    /**
     * Retrieve current certificates not expired
     *
     * @param limit
     */
    public List<CertificateElement> retrieveCurrentCertificates() {
        List<CertificateElement> result = new ArrayList<>();
        for (CertificateElement certificate : certificates) {
            if (!certificate.isExpired() && certificate.getNotBefore().before(checkDate)) {
                result.add(certificate);
            }
        }
        return result;
    }

    /**
     * Retrieve certificates that expire in @limit days
     *
     * @param limit
     */
    public List<CertificateElement> retrieveCertificatesLimitReach() {
        List<CertificateElement> result = new ArrayList<>();
        for (CertificateElement certificate : certificates) {
            if (getCertificateLimitMax() >= certificate.getExpirationIn()) {
                result.add(certificate);
            }
        }
        return result;
    }

    /**
     * Get certificate with a not before date pass (even if expired)
     */
    public List<CertificateElement> getCertificatesNotBeforePast() {
        List<CertificateElement> certificatesActive = new ArrayList<>();
        for (CertificateElement certificate : certificates) {
            if (certificate.getNotBefore().before(checkDate)) {
                certificatesActive.add(certificate);
            }
        }
        return certificatesActive;
    }

    // ----- ----- GETTER & SETTER ----- -----//

    /**
     * Get the certificate max limit
     */
    public int getCertificateLimitMax() {
        return certificateLimits.get(0);
    }

    public DBTrustedLists getTl() {
        return tl;
    }

    public void setTl(DBTrustedLists tl) {
        this.tl = tl;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public List<Integer> getCertificateLimits() {
        return certificateLimits;
    }

    public void setCertificateLimits(List<Integer> certificateLimits) {
        this.certificateLimits = certificateLimits;
    }

    public List<CertificateElement> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateElement> certificates) {
        this.certificates = certificates;
    }

    public BreakType getBreakType() {
        return breakType;
    }

    public void setBreakType(BreakType breakType) {
        this.breakType = breakType;
    }

    public BreakElement getNextUpdateDateElement() {
        return nextUpdateDateElement;
    }

    public void setNextUpdateDateElement(BreakElement nextUpdateDateElement) {
        this.nextUpdateDateElement = nextUpdateDateElement;
    }

    public CertificateBreakElement getSigningCertificateElement() {
        return signingCertificateElement;
    }

    public void setSigningCertificateElement(CertificateBreakElement signingCertificateElement) {
        this.signingCertificateElement = signingCertificateElement;
    }

    public CertificateBreakElement getTwoCertificatesElement() {
        return twoCertificatesElement;
    }

    public void setTwoCertificatesElement(CertificateBreakElement twoCertificatesElement) {
        this.twoCertificatesElement = twoCertificatesElement;
    }

    public CertificateBreakElement getShiftedPeriodElement() {
        return shiftedPeriodElement;
    }

    public void setShiftedPeriodElement(CertificateBreakElement shiftedPeriodElement) {
        this.shiftedPeriodElement = shiftedPeriodElement;
    }

}
