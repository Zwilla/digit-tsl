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
package eu.europa.ec.joinup.tsl.business.dto.tl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.tsl.KeyUsageBit;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;

public class TLCertificate extends AbstractTLDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLCertificate.class);

    private byte[] certEncoded;

    @JsonIgnore
    @XStreamOmitField
    private CertificateToken token;
    private String certB64;
    private String certSubjectShortName;
    private String certSubject;
    private String certSerial;
    private String certIssuer;
    private String certDigestAlgo;
    private Date certNotBefore;
    private Date certAfter;
    private String certificateInfo;
    private List<String> keyUsageList;
    private List<String> extendedKeyUsageList;
    private byte[] certSki;
    private String certSkiB64;

    public TLCertificate() {
    }

    public TLCertificate(int iddb, String location, byte[] encodedCertificate) {
        super(iddb, location);
        if (encodedCertificate != null) {

            try {
                CertificateToken cert = DSSUtils.loadCertificate(encodedCertificate);
                if (cert != null) {
                    certEncoded = cert.getEncoded();
                    token = cert;
                    certB64 = Utils.toBase64(cert.getEncoded());
                    certSubject = cert.getSubjectX500Principal().toString();
                    certSerial = cert.getSerialNumber().toString();
                    certIssuer = cert.getIssuerX500Principal().toString();
                    certSubjectShortName = CertificateTokenUtils.getSubjectName(cert);
                    certDigestAlgo = cert.getDigestAlgorithm().getName();
                    certAfter = cert.getNotAfter();
                    certNotBefore = cert.getNotBefore();
                    try {
                        certificateInfo = cert.getCertificate().toString();
                    } catch (IllegalStateException e) {
                        // TODO: Catch IllegalArgument and/or IllegalState ?
                        certificateInfo = "";
                    }
                    byte[] ski = TLUtils.getSki(cert);
                    if (ArrayUtils.isNotEmpty(ski)) {
                        certSki = ski;
                        certSkiB64 = Base64.encodeBase64String(ski);
                    }
                    setKeyUsageList(new ArrayList<String>());
                    if (cert.getKeyUsageBits() != null) {
                        for (KeyUsageBit kub : cert.getKeyUsageBits()) {
                            getKeyUsageList().add(kub.name());
                        }
                    }

                    setExtendedKeyUsageList(new ArrayList<String>());
                    if (cert.getCertificate().getExtendedKeyUsage() != null) {
                        for (String str : cert.getCertificate().getExtendedKeyUsage()) {
                            getExtendedKeyUsageList().add(str);
                        }
                    }

                }
            } catch (Exception e) {
                token = null;
                if (e.getMessage().equals("invalid info structure in RSA public key")) {
                    LOGGER.error("invalid info structure in RSA public key : " + Base64.encodeBase64String(encodedCertificate));
                } else {
                    LOGGER.error("Unable to parse certificate '" + Base64.encodeBase64String(encodedCertificate) + "' : " + e.getMessage(), e);
                }
            }
        }
    }

    public TLCertificate(byte[] input) {
        this(0, "0", input);
    }

    public TLDifference asPublishedDiff(TLCertificate publishedTl) {
        TLDifference myDiff = null;
        if (publishedTl.getCertB64() != null) {
            if ((getCertB64() != null) && !(getCertB64().equalsIgnoreCase(publishedTl.getCertB64()))) {
                myDiff = new TLDifference(getId(), publishedTl.getCertB64(), getCertB64());
            }
        } else {
            myDiff = new TLDifference(getId(), "", getCertSubjectShortName());
        }

        return myDiff;
    }

    public void setTokenFromEncoded() {
        setToken(DSSUtils.loadCertificate(getCertEncoded()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((certB64 == null) ? 0 : certB64.hashCode());
        result = (prime * result) + Arrays.hashCode(certEncoded);
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
        TLCertificate other = (TLCertificate) obj;

        if (certB64 == null) {
            if (other.certB64 != null) {
                return false;
            }
        } else if (!certB64.equals(other.certB64)) {
            return false;
        }
        if (!Arrays.equals(certEncoded, other.certEncoded)) {
            return false;
        }
        return true;
    }

    public byte[] getCertEncoded() {
        return certEncoded;
    }

    public void setCertEncoded(byte[] certEncoded) {
        this.certEncoded = certEncoded;
    }

    public CertificateToken getToken() {
        return token;
    }

    public void setToken(CertificateToken token) {
        this.token = token;
    }

    public String getCertB64() {
        return certB64;
    }

    public void setCertB64(String certB64) {
        this.certB64 = certB64;
    }

    public String getCertSubjectShortName() {
        return StringUtils.replace(certSubjectShortName, "\\", "");
        // return certSubjectShortName;
    }

    public void setCertSubjectShortName(String certSubjectShortName) {
        this.certSubjectShortName = certSubjectShortName;
    }

    public String getCertSubject() {
        return certSubject;
        // return certSubject;
    }

    public void setCertSubject(String certSubject) {
        this.certSubject = certSubject;
    }

    public String getCertSerial() {
        return certSerial;
    }

    public void setCertSerial(String certSerial) {
        this.certSerial = certSerial;
    }

    public String getCertIssuer() {
        return certIssuer;
        // return certIssuer;
    }

    public void setCertIssuer(String certIssuer) {
        this.certIssuer = certIssuer;
    }

    public String getCertDigestAlgo() {
        return certDigestAlgo;
    }

    public void setCertDigestAlgo(String certDigestAlgo) {
        this.certDigestAlgo = certDigestAlgo;
    }

    public Date getCertNotBefore() {
        return certNotBefore;
    }

    public void setCertNotBefore(Date certNotBefore) {
        this.certNotBefore = certNotBefore;
    }

    public Date getCertAfter() {
        return certAfter;
    }

    public void setCertAfter(Date certAfter) {
        this.certAfter = certAfter;
    }

    public String getCertificateInfo() {
        return certificateInfo;
    }

    public void setCertificateInfo(String certificateInfo) {
        this.certificateInfo = certificateInfo;
    }

    public List<String> getKeyUsageList() {
        return keyUsageList;
    }

    public void setKeyUsageList(List<String> keyUsageList) {
        this.keyUsageList = keyUsageList;
    }

    public List<String> getExtendedKeyUsageList() {
        return extendedKeyUsageList;
    }

    public void setExtendedKeyUsageList(List<String> extendedKeyUsageList) {
        this.extendedKeyUsageList = extendedKeyUsageList;
    }

    public byte[] getCertSki() {
        return certSki;
    }

    public void setCertSki(byte[] certSki) {
        this.certSki = certSki;
    }

    public String getCertSkiB64() {
        return certSkiB64;
    }

    public void setCertSkiB64(String certSkiB64) {
        this.certSkiB64 = certSkiB64;
    }

    public String certToString() {
        return getCertSubjectShortName() + " : " + TLUtils.toStringFormat(getCertNotBefore()) + " - " + TLUtils.toStringFormat(getCertAfter());
    }

}
