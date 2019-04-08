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
package eu.europa.ec.joinup.tsl.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;

@Entity
@Table(name = "TL_SIGNATURE_INFORMATIONS")
@GenericGenerator(name = "file_id_fk", strategy = "foreign", parameters = @Parameter(name = "property", value = "file"))
public class DBSignatureInformation {

    @Id
    @Column(name = "FILE_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "file_id_fk")
    private Integer fileId;

    @OneToOne(mappedBy = "signatureInformation", cascade = CascadeType.ALL)
    private DBFiles file;

    @Column(name = "INDICATION")
    @Enumerated(EnumType.STRING)
    private SignatureStatus indication;

    @Column(name = "SUB_INDICATION")
    private String subIndication;

    @Column(name = "SIGNING_DATE")
    private Date signingDate;
    @Column(name = "SIGNATURE_FORMAT")
    private String signatureFormat;

    @Column(name = "SIGNED_BY")
    private String signedBy;

    @Column(name = "SIGNED_BY_NOT_BEFORE")
    private Date signedByNotBefore;

    @Column(name = "SIGNED_BY_NOT_AFTER")
    private Date signedByNotAfter;

    @Column(name = "DIGEST_ALGORITHM")
    private String digestAlgo;

    @Column(name = "ENCRYPTION_ALGORITHM")
    private String encryptionAlgo;

    @Column(name = "KEY_LENGTH")
    private int keyLength;

    public DBSignatureInformation() {
    }

    public DBFiles getFile() {
        return file;
    }

    public void setFile(DBFiles file) {
        this.file = file;
    }

    public SignatureStatus getIndication() {
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

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((fileId == null) ? 0 : fileId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DBSignatureInformation other = (DBSignatureInformation) obj;
        if (fileId == null) {
            if (other.fileId != null) {
                return false;
            }
        } else if (!fileId.equals(other.fileId)) {
            return false;
        }
        return true;
    }

}
