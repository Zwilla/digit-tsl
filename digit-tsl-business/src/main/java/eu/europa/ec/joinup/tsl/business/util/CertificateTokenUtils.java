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

import java.util.ResourceBundle;

import org.bouncycastle.asn1.x500.style.BCStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.esig.dss.DSSASN1Utils;
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

}
