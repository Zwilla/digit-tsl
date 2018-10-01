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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.service.TLBuilder;
import eu.europa.ec.joinup.tsl.business.service.TrustedListJaxbService;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;

public class CertificateExtractorTest extends AbstractSpringTest {

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void extractCertificateFromLOTL() throws XmlMappingException, IOException {
        TL tl = tlBuilder.buildTLV5(1, jaxbService.unmarshallTSLV5(new File("src/test/resources/tsl/EU/LOTL.xml")));

        StringBuilder extractor = new StringBuilder();
        if ((tl != null) && !CollectionUtils.isEmpty(tl.getPointers())) {
            for (TLPointersToOtherTSL pointer : tl.getPointers()) {
                if (pointer.getMimeType().equals(MimeType.XML)) {
                    if (!CollectionUtils.isEmpty(pointer.getServiceDigitalId())) {
                        for (TLDigitalIdentification digit : pointer.getServiceDigitalId()) {
                            if (!CollectionUtils.isEmpty(digit.getCertificateList())) {
                                for (TLCertificate certificate : digit.getCertificateList()) {
                                    extractor.append(extractCertificateInfo(pointer.getSchemeTerritory(), certificate));
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(extractor.toString());
    }

    private String extractCertificateInfo(String territory, TLCertificate certificate) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        return territory + "|" + certificate.getCertSubject() + "|" + dt.format(certificate.getCertNotBefore()) + "|" + dt.format(certificate.getCertAfter()) + "\n";
    }

}
