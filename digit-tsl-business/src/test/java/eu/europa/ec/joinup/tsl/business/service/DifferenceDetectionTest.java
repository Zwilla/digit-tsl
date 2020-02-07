package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.model.enums.Tag;
import eu.europa.esig.jaxb.tsl.TrustStatusListType;

public class DifferenceDetectionTest extends AbstractSpringTest {

    @Autowired
    private TLBuilder tlBuilder;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Test
    public void testDifferenceDetection() throws Exception {
        TrustStatusListType unmarshall = jaxbService.unmarshallTSL(new File("src/test/resources/lotl.xml"));
        TL tl = tlBuilder.buildTLV4(1, unmarshall);
        TL tl2 = tlBuilder.buildTLV4(1, unmarshall);

        List<TLDifference> difList = tl.asPublishedDiff(tl2);
        for (TLDifference dif : difList) {
            System.out.println("***** : " + dif.getId() + " / " + dif.getPublishedValue());
        }

        assertTrue(CollectionUtils.isEmpty(tl.asPublishedDiff(tl2)));
        assertTrue(CollectionUtils.isNotEmpty(tl2.getPointers()));
        assertTrue(CollectionUtils.isEmpty(tl.getPointersDiff(tl2.getPointers(), tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL)));

        assertTrue(CollectionUtils.isNotEmpty(tl2.getPointers()));
        tl2.getSchemeInformation().setIssueDate(new Date());
        List<TLDifference> diffs = tl.asPublishedDiff(tl2);
        assertEquals(1, CollectionUtils.size(diffs)); // SCHEME_INFO + ISSUE_DATE

        List<TLPointersToOtherTSL> pointers = tl2.getPointers();
        assertTrue(CollectionUtils.isNotEmpty(pointers));
        pointers.get(0).setTlLocation("XX");

        List<TLDifference> pointersDiff = tl.getPointersDiff(tl2.getPointers(), tl.getId() + "_" + Tag.POINTERS_TO_OTHER_TSL);
        assertEquals(1, CollectionUtils.size(pointersDiff)); // Location
    }

}
