package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class TLPointersToOtherTSLTest extends AbstractSpringTest {

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void testConstructor() throws Exception {
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));

        TLPointersToOtherTSL tlPointers = null;
        if (unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer() != null) {
            tlPointers = new TLPointersToOtherTSL(0, "test", unmarshall.getSchemeInformation().getPointersToOtherTSL().getOtherTSLPointer().get(0));
        }

        assert tlPointers != null;
        assertTrue(StringUtils.isNotEmpty(tlPointers.getTlLocation()));
    }

}
