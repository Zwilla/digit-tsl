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

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class TSLExtractorTest extends AbstractSpringTest {

    @Autowired
    private TSLExtractor extractor;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void getTLPointers() throws Exception {
        FileInputStream is = new FileInputStream("src/test/resources/lotl.xml");
        TrustStatusListType tsl = jaxbService.unmarshallTSL(is);
        List<TLPointersToOtherTSL> tlPointers = extractor.getTLPointers(tsl);
        assertTrue(CollectionUtils.isNotEmpty(tlPointers));

        boolean detectPdf = false;
        boolean detectXml = false;
        boolean detectBelgium = false;
        for (TLPointersToOtherTSL pointer : tlPointers) {
            if ("BE".equals(pointer.getSchemeTerritory())) {
                detectBelgium = true;
            }
            if (MimeType.XML.equals(pointer.getMimeType())) {
                detectXml = true;
            }
            if (MimeType.PDF.equals(pointer.getMimeType())) {
                detectPdf = true;
            }
        }
        assertTrue(detectPdf);
        assertTrue(detectXml);
        assertTrue(detectBelgium);

        IOUtils.closeQuietly(is);
    }

}
