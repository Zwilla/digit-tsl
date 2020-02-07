package eu.europa.ec.joinup.tsl.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLDraftServiceTest extends AbstractSpringTest {

    private static final String EU_COUNTRY_CODE = "EU";

    @Autowired
    private TLService tlService;

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private DraftStoreService draftStoreService;

    @Test
    public void cloneTLtoDraft() {

        Load load = new Load();
        tlLoader.loadTL(EU_COUNTRY_CODE, getLOTLUrl(), TLType.LOTL, TLStatus.PROD, load);
        tlLoader.check(load);

        List<TrustedListsReport> listProd = tlService.getAllProdTlReports();
        assertEquals(1, listProd.size());

        DBTrustedLists draftTSL = draftService.cloneTLtoDraft(EU_COUNTRY_CODE, draftStoreService.getNewDraftStore(), "test-man", new Load());
        assertNotNull(draftTSL);

        tlValidator.validateTLSignature(draftTSL);
        TrustedListsReport report = tlService.getTLInfo(draftTSL.getId());
        assertNotNull(report);

        assertEquals(EU_COUNTRY_CODE, draftTSL.getTerritory().getCodeTerritory());
        assertEquals(TLStatus.DRAFT, draftTSL.getStatus());
        assertEquals(TLType.LOTL, draftTSL.getType());

        assertNotNull(draftTSL.getIssueDate());
        assertNotNull(draftTSL.getNextUpdateDate());
        assertTrue(StringUtils.isNotEmpty(draftTSL.getName()));

    }

    @Test
    public void createDraftFromBinaries() throws Exception {
        byte[] byteArray = IOUtils.toByteArray(new FileInputStream("src/test/resources/tsl/AT/2016-10-13_13-09-04.xml"));
        DBTrustedLists draftTSL = draftService.createDraftFromXML(byteArray, draftStoreService.getNewDraftStore(), "test-man", new Load());
        assertNotNull(draftTSL);
        assertEquals("AT", draftTSL.getTerritory().getCodeTerritory());
        assertEquals(TLType.TL, draftTSL.getType());
        assertEquals(TLStatus.DRAFT, draftTSL.getStatus());
        assertEquals(29, draftTSL.getSequenceNumber());
        assertNotNull(draftTSL.getIssueDate());
        assertNotNull(draftTSL.getNextUpdateDate());
        assertTrue(StringUtils.isNotEmpty(draftTSL.getName()));

        tlValidator.validateTLSignature(draftTSL);
        TrustedListsReport report = tlService.getTLInfo(draftTSL.getId());
        assertNotNull(report);
    }

}
