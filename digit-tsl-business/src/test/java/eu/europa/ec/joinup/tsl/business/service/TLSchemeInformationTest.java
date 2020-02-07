package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class TLSchemeInformationTest extends AbstractSpringTest {

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void testConstructor() throws Exception {
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));

        TLSchemeInformation schemeInfo = null;
        if (unmarshall.getSchemeInformation() != null) {
            schemeInfo = new TLSchemeInformation(0, unmarshall.getSchemeInformation());
        }

        assert schemeInfo != null;
        assertTrue(schemeInfo.getTlIdentifier() > 0);
        assertTrue(schemeInfo.getSequenceNumber() > 0);
        assertTrue(StringUtils.isNotEmpty(schemeInfo.getTerritory()));
    }

}
