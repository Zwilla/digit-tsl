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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tlcc.TLCCResults;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.service.TLBuilder;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TrustedListJaxbService;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.jaxb.v5.tsl.TrustStatusListTypeV5;

public class TLCCUtilsTest extends AbstractSpringTest {

    @Autowired
    TLService tlService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private TLBuilder tlBuilder;

    @Test
    public void parseAT() throws Exception {
        Assert.assertEquals(1649, parseTLCC("AT"));
    }

    @Test
    public void parseBE() throws Exception {
        Assert.assertEquals(799, parseTLCC("BE"));
    }

    @Test
    public void parseBG() throws Exception {
        Assert.assertEquals(1004, parseTLCC("BG"));
    }

    @Test
    public void parseCY() throws Exception {
        Assert.assertEquals(131, parseTLCC("CY"));
    }

    @Test
    public void parseCZ() throws Exception {
        Assert.assertEquals(2621, parseTLCC("CZ"));
    }

    @Test
    public void parseDE() throws Exception {
        Assert.assertEquals(32351, parseTLCC("DE"));
    }

    @Test
    public void parseDK() throws Exception {
        Assert.assertEquals(209, parseTLCC("DK"));
    }

    @Test
    public void parseEE() throws Exception {
        Assert.assertEquals(1844, parseTLCC("EE"));
    }

    @Test
    public void parseEL() throws Exception {
        Assert.assertEquals(1078, parseTLCC("EL"));
    }

    @Test
    public void parseES() throws Exception {
        Assert.assertEquals(7195, parseTLCC("ES"));
    }

    @Test
    public void parseFI() throws Exception {
        Assert.assertEquals(304, parseTLCC("FI"));
    }

    @Test
    public void parseFR() throws Exception {
        Assert.assertEquals(7752, parseTLCC("FR"));
    }

    @Test
    public void parseHR() throws Exception {
        Assert.assertEquals(798, parseTLCC("HR"));
    }

    @Test
    public void parseHU() throws Exception {
        Assert.assertEquals(9257, parseTLCC("HU"));
    }

    @Test
    public void parseIS() throws Exception {
        Assert.assertEquals(216, parseTLCC("IS"));
    }

    @Test
    public void parseIE() throws Exception {
        Assert.assertEquals(208, parseTLCC("IE"));
    }

    @Test
    public void parseIT() throws Exception {
        Assert.assertEquals(15752, parseTLCC("IT"));
    }

    @Test
    public void parseLI() throws Exception {
        Assert.assertEquals(780, parseTLCC("LI"));
    }

    @Test
    public void parseLT() throws Exception {
        Assert.assertEquals(2580, parseTLCC("LT"));
    }

    @Test
    public void parseLU() throws Exception {
        Assert.assertEquals(669, parseTLCC("LU"));
    }

    @Test
    public void parseLV() throws Exception {
        Assert.assertEquals(900, parseTLCC("LV"));
    }

    @Test
    public void parseMT() throws Exception {
        Assert.assertEquals(344, parseTLCC("MT"));
    }

    @Test
    public void parseNL() throws Exception {
        Assert.assertEquals(4168, parseTLCC("NL"));
    }

    @Test
    public void parseNO() throws Exception {
        Assert.assertEquals(1831, parseTLCC("NO"));
    }

    @Test
    public void parsePL() throws Exception {
        Assert.assertEquals(6039, parseTLCC("PL"));
    }

    @Test
    public void parsePT() throws Exception {
        Assert.assertEquals(1834, parseTLCC("PT"));
    }

    @Test
    public void parseRO() throws Exception {
        Assert.assertEquals(2665, parseTLCC("RO"));
    }

    @Test
    public void parseSE() throws Exception {
        Assert.assertEquals(376, parseTLCC("SE"));
    }

    @Test
    public void parseSK() throws Exception {
        Assert.assertEquals(2330, parseTLCC("SK"));
    }

    @Test
    public void parseSI() throws Exception {
        Assert.assertEquals(2087, parseTLCC("SI"));
    }

    @Test
    public void parseUK() throws Exception {
        Assert.assertEquals(1093, parseTLCC("UK"));
    }

    // @Test
    // public void parseLOTL() throws Exception {
    // Assert.assertEquals(1662, parseTLCC("LOTL"));
    // }

    private int parseTLCC(String cc) throws Exception {
        String str = org.apache.commons.io.FileUtils.readFileToString(new File("src/test/resources/TLCC/result" + cc + ".xml"), StandardCharsets.UTF_8);
        TLCCResults tlccObj = TLCCMarshallerUtils.unmarshallTlccXml(str);
        Assert.assertNotNull(tlccObj);
        List<CheckResultDTO> checks = TLCCParserUtils.parseAllTLCC(tlccObj, "1");
        Assert.assertNotNull(checks);
        Assert.assertNotEquals(checks.size(), 0);

        Set<String> tlccSet = new LinkedHashSet<>();
        List<String> tlccList = new ArrayList<>();

        int lotlId = createTLinDB(TLType.TL);
        TrustStatusListTypeV5 tsl = jaxbService.unmarshallTSLV5(new File("src/test/resources/TLCC/" + cc + ".xml"));
        TL tl = tlBuilder.buildTLV5(lotlId, tsl);

        for (CheckResultDTO tlccCheck : checks) {
            tlccCheck.setLocation(LocationUtils.idUserReadable(tl, tlccCheck.getId()));
            tlccSet.add(tlccCheck.getId() + "-" + tlccCheck.getLocation());
            tlccList.add(tlccCheck.getId() + "-" + tlccCheck.getLocation());
        }
        return checks.size();
    }

    // @Test
    public void generateXSD() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(TLCCResults.class);
        SchemaOutputResolver sor = new TLCCSchemaOutputResolver();
        jaxbContext.generateSchema(sor);
    }

    private int createTLinDB(TLType type) {
        DBTrustedLists trustedList = new DBTrustedLists();
        trustedList.setType(type);
        trustedList.setXmlFile(new DBFiles());
        tlRepository.save(trustedList);
        return trustedList.getId();
    }
}
