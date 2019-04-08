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
package eu.europa.ec.joinup.tsl.checker.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.checker.config.TLCheckerSpringTest;
import eu.europa.ec.joinup.tsl.checker.dto.TLCCRequestDTO;

public class TLCCIntegrationTest extends TLCheckerSpringTest {

    private static final List<String> countries = Arrays.asList("AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "EL", "ES", "FI", "FR", "HR", "HU", "IE", "IS", "IT", "LI", "LOTL", "LT", "LU", "LV",
            "MT", "NL", "NO", "PL", "PT", "RO", "SE", "SI", "SK", "UK");

    @Autowired
    RunTLCC runTLCC;

    @Test
    public void getResults() throws IOException {
        for (String country : countries) {
            getTlccResult(country);
        }
    }

    @Test
    public void getFakeEU() throws IOException {
        getTlccResult("FAKE_EU");
    }

    private void getTlccResult(String countryCode) throws IOException {
        TLCCRequestDTO requestDTO = new TLCCRequestDTO();
        requestDTO.setTlId(1);
        requestDTO.setTlXmlPath("src/test/resources/tsl/" + countryCode + ".xml");
        requestDTO.setLotlPath("src/test/resources/tsl/LOTL.xml");

        String resultTlcc = runTLCC.executeAllChecks(requestDTO, "TRUSTED_LIST");
        Assert.assertNotNull(resultTlcc);
        FileUtils.writeStringToFile(new File("src/test/resources/resultTlcc/result" + countryCode + ".xml"), resultTlcc, StandardCharsets.UTF_8);
    }

}
