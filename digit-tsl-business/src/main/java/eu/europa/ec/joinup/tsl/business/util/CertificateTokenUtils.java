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
package eu.europa.ec.joinup.tsl.business.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.esig.dss.DSSASN1Utils;
import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateTokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLService.class);

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    /**
     * Get certificate subject short name if defined or subject name
     *
     * @param certificate
     */
    public static String getSubjectName(CertificateToken certificate) {
        try {
            String subjectName = DSSASN1Utils.extractAttributeFromX500Principal(BCStyle.CN, certificate.getSubjectX500Principal());
            if (subjectName == null) {
                subjectName = certificate.getSubjectX500Principal().toString();
            }
            return subjectName;
        } catch (Exception e) {
            LOGGER.error("Error while getting certificate subject short name", e);
            return bundle.getString("");
        }
    }

    /**
     * Load certificate from file
     * 
     * @param fileCertificate
     */
    public static CertificateToken loadCertificate(File fileCertificate) {
        if (fileCertificate != null) {
            try {
                return loadCertificate(FileUtils.readFileToByteArray(fileCertificate));
            } catch (IOException e1) {
                LOGGER.debug("Error while reading file: " + fileCertificate.getName(), e1);
            }
        }
        return null;
    }

    /**
     * Load certificate from B64
     * 
     * @param certificateB64
     */
    public static CertificateToken loadCertificate(String certificateB64) {
        if (!StringUtils.isEmpty(certificateB64)) {
            return loadCertificate(Base64.decodeBase64(certificateB64));
        }

        return null;
    }

    /**
     * Load certificate from encoded certificate
     * 
     * @param encodedCertificate
     * @return
     */
    public static CertificateToken loadCertificate(byte[] encodedCertificate) {
        if (encodedCertificate != null) {
            try {
                return DSSUtils.loadCertificate(encodedCertificate);
            } catch (Exception e) {
                return loadCertificateWithJVM(encodedCertificate);
            }
        }
        return null;

    }

    /**
     * Load certificate from JVM
     * 
     * @param encodedCertificate
     * @return
     */
    private static CertificateToken loadCertificateWithJVM(byte[] encodedCertificate) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(encodedCertificate));
            return new CertificateToken(certificate);
        } catch (CertificateException e1) {
            LOGGER.error("Unable to parse certificate with JVM: " + Base64.encodeBase64String(encodedCertificate));
            LOGGER.debug("Exception : ", e1);
        }
        return null;
    }

    /**
     * Init new list to sort it (input unmodifiable)
     * 
     * @param list
     */
    public static List<CertificateToken> sortCertificateList(List<CertificateToken> list) {
        List<CertificateToken> results = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (CertificateToken certificate : list) {
                results.add(certificate);
            }
            Collections.sort(results, new Comparator<CertificateToken>() {
                @Override
                public int compare(CertificateToken o1, CertificateToken o2) {
                    return o1.getNotAfter().compareTo(o2.getNotAfter());
                }
            });
        }
        return results;
    }
}
