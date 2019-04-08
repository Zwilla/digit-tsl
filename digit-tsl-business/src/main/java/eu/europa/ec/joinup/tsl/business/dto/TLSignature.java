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
package eu.europa.ec.joinup.tsl.business.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBSignatureInformation;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

public class TLSignature {

    private SignatureStatus indication;

    private String subIndication;

    private Date signingDate;

    private String signatureFormat;

    private String signedBy;

    private Date signedByNotBefore;

    private Date signedByNotAfter;

    private String digestAlgo;

    private String encryptionAlgo;

    private int keyLength;

    public TLSignature() {

    }

    public TLSignature(DBSignatureInformation db) {
        setDigestAlgo(db.getDigestAlgo());
        setEncryptionAlgo(db.getEncryptionAlgo());
        setIndication(db.getIndication());
        setKeyLength(db.getKeyLength());
        setSignatureFormat(db.getSignatureFormat());
        setSignedBy(db.getSignedBy());
        setSignedByNotAfter(db.getSignedByNotAfter());
        setSignedByNotBefore(db.getSignedByNotBefore());
        setSigningDate(db.getSigningDate());
        setSubIndication(db.getSubIndication());
    }

    public SignatureStatus getIndication() {
        if (indication == null) {
            return SignatureStatus.INDETERMINATE;
        }
        return indication;
    }

    public void setIndication(SignatureStatus indication) {
        this.indication = indication;
    }

    public String getSubIndication() {
        return subIndication;
    }

    public void setSubIndication(String subIndication) {
        this.subIndication = subIndication;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }

    public String getSignatureFormat() {
        return signatureFormat;
    }

    public void setSignatureFormat(String signatureFormat) {
        this.signatureFormat = signatureFormat;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public Date getSignedByNotBefore() {
        return signedByNotBefore;
    }

    public void setSignedByNotBefore(Date signedByNotBefore) {
        this.signedByNotBefore = signedByNotBefore;
    }

    public Date getSignedByNotAfter() {
        return signedByNotAfter;
    }

    public void setSignedByNotAfter(Date signedByNotAfter) {
        this.signedByNotAfter = signedByNotAfter;
    }

    public String getDigestAlgo() {
        return digestAlgo;
    }

    public void setDigestAlgo(String digestAlgo) {
        this.digestAlgo = digestAlgo;
    }

    public String getEncryptionAlgo() {
        return encryptionAlgo;
    }

    public void setEncryptionAlgo(String encryptionAlgo) {
        this.encryptionAlgo = encryptionAlgo;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public List<TLDifference> asPublishedDiff(TLSignature comparedSignature, String parent) {
        List<TLDifference> differences = new ArrayList<>();
        if ((comparedSignature == null) || (comparedSignature.getIndication() == null)) {
            differences.add(new TLDifference(parent, "", getSignedBy() + " - " + getSigningDate().toString()));
        } else if (comparedSignature.getIndication() == SignatureStatus.NOT_SIGNED) {
            differences.add(new TLDifference(parent, "", getSignedBy() + " - " + getSigningDate().toString()));
        } else if (!equals(comparedSignature)) {
            // Digest Algo
            if (!getDigestAlgo().equals(comparedSignature.getDigestAlgo())) {
                differences.add(new TLDifference(parent + "_" + Tag.DIGEST_ALGORITHM, comparedSignature.getDigestAlgo(), getDigestAlgo()));
            }
            // Encryption Algo
            if (!getEncryptionAlgo().equals(comparedSignature.getEncryptionAlgo())) {
                differences.add(new TLDifference(parent + "_" + Tag.ENCRYPTION_ALGORITHM, comparedSignature.getEncryptionAlgo(), getEncryptionAlgo()));
            }
            // Key Length
            if (!getIndication().equals(comparedSignature.getIndication())) {
                differences.add(new TLDifference(parent + "_" + Tag.INDICATION, comparedSignature.getIndication().toString(), getIndication().toString()));
            }
            // Signature Format
            if (getSignatureFormat() != null && !getSignatureFormat().equals(comparedSignature.getSignatureFormat())) {
                differences.add(new TLDifference(parent + "_" + Tag.SIGNATURE_FORMAT, comparedSignature.getSignatureFormat(), getSignatureFormat()));
            }
            // Signature By
            if (!getSignedBy().equals(comparedSignature.getSignedBy())) {
                differences.add(new TLDifference(parent + "_" + Tag.SIGNED_BY, comparedSignature.getSignedBy(), getSignedBy()));
            }
            // Signing Date

            String currentDate = getSigningDate() != null ? TLUtils.toStringFormat(getSigningDate()) : "";
            String comparedDate = comparedSignature.getSigningDate() != null ? TLUtils.toStringFormat(comparedSignature.getSigningDate()) : "";
            if (!currentDate.equals(comparedDate)) {
                differences.add(new TLDifference(parent + "_" + Tag.SIGNING_DATE, comparedDate, currentDate));
            }
        }
        return differences;
    }

}
