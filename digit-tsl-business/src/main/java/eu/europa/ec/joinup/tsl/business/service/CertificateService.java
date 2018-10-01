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

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.DLSet;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import eu.europa.esig.dss.tsl.KeyUsageBit;
import eu.europa.esig.dss.x509.CertificateToken;

@Service
public class CertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateService.class);

    private static final String SUBJECT_KEY_IDENTIFIER_OID = "2.5.29.14";

    private static final String ID_TSL_KP_TSLSIGNING_OID = "0.4.0.2231.3.0";

    public String getCountryCode(CertificateToken certificate) {
        return StringUtils.upperCase(getRDNValue(BCStyle.C, certificate));
    }

    public String getOrganization(CertificateToken certificate) {
        return StringEscapeUtils.unescapeJava(getRDNValue(BCStyle.O, certificate));
    }

    private String getRDNValue(ASN1ObjectIdentifier oid, CertificateToken certificate) {
        String value = null;
        if (certificate != null) {
            try {

                X509Certificate x509Certificate = certificate.getCertificate();
                X500Name x500name = new JcaX509CertificateHolder(x509Certificate).getSubject();
                if (x500name != null) {
                    Map<String, Object> pairs = new HashMap<>();

                    DLSequence seq = (DLSequence) DERSequence.fromByteArray(x500name.getEncoded());
                    for (int i = 0; i < seq.size(); i++) {
                        DLSet set = (DLSet) seq.getObjectAt(i);
                        for (int j = 0; j < set.size(); j++) {
                            DLSequence pair = (DLSequence) set.getObjectAt(j);
                            ASN1Encodable objectAt = pair.getObjectAt(1);
                            pairs.put(((ASN1ObjectIdentifier) pair.getObjectAt(0)).getId(), objectAt);
                        }
                    }

                    if (pairs.get(oid.toString()) instanceof ASN1String) {
                        ASN1String o = (ASN1String) pairs.get(oid.toString());
                        value = o.getString();
                    } else if (pairs.get(oid.toString()) != null) {
                        LOGGER.error("Type unknown " + pairs.get(oid.toString()).getClass());
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Unable to retrieve X500Name from certificate : " + e.getMessage(), e);
            }
        }
        return value;
    }

    public byte[] getSubjectKeyIdentifier(CertificateToken certificate) {
        byte[] subjectKeyIdentifier = null;
        if (certificate != null) {
            X509Certificate x509Certificate = certificate.getCertificate();
            subjectKeyIdentifier = x509Certificate.getExtensionValue(SUBJECT_KEY_IDENTIFIER_OID);
        }
        return subjectKeyIdentifier;
    }

    public boolean hasTslSigningExtendedKeyUsage(CertificateToken certificate) {
        if (certificate != null) {
            try {
                X509Certificate x509Certificate = certificate.getCertificate();
                List<String> extendedKeyUsages = x509Certificate.getExtendedKeyUsage();
                return CollectionUtils.isNotEmpty(extendedKeyUsages) && extendedKeyUsages.contains(ID_TSL_KP_TSLSIGNING_OID);
            } catch (Exception e) {
                LOGGER.debug("Unable to retrieve extended key usages : " + e.getMessage(), e);
            }
        }
        return false;
    }

    public boolean hasAllowedKeyUsagesBits(CertificateToken certificate) {
        if (certificate != null) {
            Set<KeyUsageBit> keyUsageBits = certificate.getKeyUsageBits();
            if (CollectionUtils.size(keyUsageBits) == 1) {
                return (keyUsageBits.contains(KeyUsageBit.digitalSignature) || keyUsageBits.contains(KeyUsageBit.nonRepudiation));
            } else if (CollectionUtils.size(keyUsageBits) == 2) {
                return (keyUsageBits.contains(KeyUsageBit.digitalSignature) && keyUsageBits.contains(KeyUsageBit.nonRepudiation));
            }
        }
        return false;
    }

    public boolean isBasicConstraintCaFalse(CertificateToken certificate) {
        if (certificate != null) {
            X509Certificate x509Certificate = certificate.getCertificate();
            int basicConstraints = x509Certificate.getBasicConstraints();
            return basicConstraints == -1;
        }
        return false;
    }

}
