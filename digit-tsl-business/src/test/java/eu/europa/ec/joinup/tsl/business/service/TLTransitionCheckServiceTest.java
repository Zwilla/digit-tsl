package eu.europa.ec.joinup.tsl.business.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@SuppressWarnings("deprecation")
public class TLTransitionCheckServiceTest extends AbstractSpringTest {

    @Autowired
    private TLTransitionCheckService tlTransitionCheckService;

    @Autowired
    private CheckService checkService;

    private TL previousTL;

    @Before
    public void initTL() {
        previousTL = new TL();
        TLSchemeInformation tlSchemeInfo = new TLSchemeInformation();
        tlSchemeInfo.setIssueDate(new Date(118, Calendar.FEBRUARY, 1));
        tlSchemeInfo.setNextUpdateDate(new Date(118, Calendar.JULY, 1));
        previousTL.setSchemeInformation(tlSchemeInfo);
    }

    @Test
    public void isIssueDateBackDated() {
        TL currentTL = new TL();
        TLSchemeInformation schemeInformation = new TLSchemeInformation();
        schemeInformation.setIssueDate(new Date(117, 12, 30));
        currentTL.setSchemeInformation(schemeInformation);
        Assert.assertTrue(tlTransitionCheckService.isIssueDateBackDated(currentTL, previousTL));
        schemeInformation.setIssueDate(new Date(118, Calendar.FEBRUARY, 2));
        Assert.assertFalse(tlTransitionCheckService.isIssueDateBackDated(currentTL, previousTL));
    }

    @Test
    public void isSigningDateUlterior() {
        Date signingDate = null;
        Date publicationDate = new Date(118, Calendar.FEBRUARY, 10);
        Assert.assertFalse(tlTransitionCheckService.isSigningDateUlterior(null, publicationDate));
        signingDate = new Date(117, Calendar.FEBRUARY, 1);
        Assert.assertFalse(tlTransitionCheckService.isSigningDateUlterior(signingDate, publicationDate));
        signingDate = new Date(118, Calendar.FEBRUARY, 12);
        Assert.assertTrue(tlTransitionCheckService.isSigningDateUlterior(signingDate, publicationDate));
        signingDate = new Date(118, Calendar.FEBRUARY, 10);
        Assert.assertFalse(tlTransitionCheckService.isSigningDateUlterior(signingDate, publicationDate));
    }

    @Test
    public void initServiceTransitionEntry() throws XmlMappingException, IOException {
        Map<String, DBCheck> transitionChecks = checkService.getCheckMapByType(Tag.TRANSITION_CHECK);

        TL previousTL = fileToTL(1, "src/test/resources/tsl/BE-TEST/BE_39.xml");

        TL currentTL = fileToTL(2, "src/test/resources/tsl/BE-TEST/BE_40.xml");

        List<CheckResultDTO> tspCheck = tlTransitionCheckService.getServicesCheck(currentTL.getServiceProviders().get(0), previousTL.getServiceProviders().get(0), new Date(), transitionChecks);
        Assert.assertEquals(7, tspCheck.size());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_3", tspCheck.get(0).getId());
        Assert.assertEquals("TRANSITION_CHECK.HISTORY_CHANGE", tspCheck.get(0).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_4", tspCheck.get(1).getId());
        Assert.assertEquals("TRANSITION_CHECK.HISTORY_CHANGE", tspCheck.get(1).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_5", tspCheck.get(2).getId());
        Assert.assertEquals("TRANSITION_CHECK.HISTORY_CHANGE", tspCheck.get(2).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_1", tspCheck.get(3).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_PUBLICATION_DATE", tspCheck.get(3).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_4", tspCheck.get(4).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_PUBLICATION_DATE", tspCheck.get(4).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1_TSP_SERVICE_5", tspCheck.get(5).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_PUBLICATION_DATE", tspCheck.get(5).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_1", tspCheck.get(6).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_DELETE", tspCheck.get(6).getCheckId());

        tspCheck = tlTransitionCheckService.getServicesCheck(currentTL.getServiceProviders().get(1), previousTL.getServiceProviders().get(1), new Date(), transitionChecks);
        Assert.assertEquals(2, tspCheck.size());
        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_2_TSP_SERVICE_1", tspCheck.get(0).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_UPDATE_NO_HISTORY", tspCheck.get(0).getCheckId());

        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_2_TSP_SERVICE_1", tspCheck.get(1).getId());
        Assert.assertEquals("TRANSITION_CHECK.HISTORY_CHANGE", tspCheck.get(1).getCheckId());

        tspCheck = tlTransitionCheckService.getServicesCheck(currentTL.getServiceProviders().get(2), previousTL.getServiceProviders().get(2), new Date(), transitionChecks);
        Assert.assertEquals("2_TSP_SERVICE_PROVIDER_3_TSP_SERVICE_3", tspCheck.get(0).getId());
        Assert.assertEquals("TRANSITION_CHECK.SERVICE_PUBLICATION_DATE", tspCheck.get(0).getCheckId());
    }

    @Test
    public void testEE() throws XmlMappingException, IOException {
        Map<String, DBCheck> transitionChecks = checkService.getCheckMapByType(Tag.TRANSITION_CHECK);

        TL previousTL = fileToTL(1, "src/test/resources/tsl/EE-TEST/EE_43.xml");
        TL currentTL = fileToTL(2, "src/test/resources/tsl/EE-TEST/EE_44.xml");

        List<CheckResultDTO> tspCheck = tlTransitionCheckService.getServicesCheck(currentTL.getServiceProviders().get(0), previousTL.getServiceProviders().get(0), new Date(), transitionChecks);
        Assert.assertNotNull(tspCheck);
    }

    @Ignore
    public void zTSPChecks() throws IOException {
        TL comparedTL = fileToTL(1, "src/test/resources/transition/NL_41.xml");
        TL currentTL = fileToTL(2, "src/test/resources/transition/NL_42.xml");
        Date publicationDate = new Date(119, Calendar.APRIL, 23);
        Map<String, DBCheck> transitionChecks = checkService.getCheckMapByType(Tag.TRANSITION_CHECK);
        List<CheckResultDTO> checks = tlTransitionCheckService.performTSPChecks(currentTL, comparedTL, transitionChecks, publicationDate);
        Assert.assertTrue(checks.isEmpty());
    }
}
