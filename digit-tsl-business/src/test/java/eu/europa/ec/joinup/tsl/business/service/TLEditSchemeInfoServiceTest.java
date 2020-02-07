package eu.europa.ec.joinup.tsl.business.service;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLSchemeInformation;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

import static org.junit.Assert.*;

@Transactional
public class TLEditSchemeInfoServiceTest extends AbstractSpringTest {

    private static final String EU_COUNTRY_CODE = "EU";
    private static final String BE_COUNTRY_CODE = "BE";
    private static final int SN = 123;

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLService tlService;

    @Autowired
    private TlEditSchemeInfoService tlEditSchemeInfoService;

    @Before
    public void initialize() {
        Load load = new Load();
        tlLoader.loadTL(EU_COUNTRY_CODE, getLOTLUrl(), TLType.LOTL, TLStatus.DRAFT, load);
        tlLoader.loadTL(BE_COUNTRY_CODE, "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);

        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void editTerritory() {
        TL lotl = tlService.getTL(1);

        TLSchemeInformation tlScheme = new TLSchemeInformation();
        tlScheme.setTerritory(BE_COUNTRY_CODE);

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), tlScheme, Tag.TERRITORY.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbLOTL.getTerritory().getCodeTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));
    }

    @Test
    public void editSN() {
        TL lotl = tlService.getTL(1);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));

        TLSchemeInformation tlScheme = new TLSchemeInformation();
        tlScheme.setSequenceNumber(SN);

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), tlScheme, Tag.SEQUENCE_NUMBER.toString(), "");
        // CHECK RETURN
        assertEquals((int) schemeInfoUpdated.getSequenceNumber(), SN);

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        // CHECK DB
        assertEquals(dbLOTL.getSequenceNumber(), SN);

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertEquals((int) lotlUpdated.getSchemeInformation().getSequenceNumber(), SN);
    }

    @Test
    public void editIssueDate() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.ISSUE_DATE.toString(), "");
        // CHECK RETURN
        assertEquals(schemeInfoUpdated.getIssueDate(), detl.getSchemeInformation().getIssueDate());

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        DBTrustedLists dbDE = tlService.getDbTL(2);
        // CHECK DB
        assertEquals(dbLOTL.getIssueDate(), dbDE.getIssueDate());

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertEquals(lotlUpdated.getSchemeInformation().getIssueDate(), detl.getSchemeInformation().getIssueDate());
    }

    @Test
    public void editNextDate() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.NEXT_UPDATE.toString(), "");
        // CHECK RETURN
        assertEquals(schemeInfoUpdated.getNextUpdateDate(), detl.getSchemeInformation().getNextUpdateDate());

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        DBTrustedLists dbDE = tlService.getDbTL(2);
        // CHECK DB
        assertEquals(dbLOTL.getNextUpdateDate(), dbDE.getNextUpdateDate());

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertEquals(lotlUpdated.getSchemeInformation().getNextUpdateDate(), detl.getSchemeInformation().getNextUpdateDate());
        // assertFalse(lotlUpdated.getSchemeInformation().getIssueDate().equals(detl.getSchemeInformation().getIssueDate()));
    }

    @Test
    public void editSchemeOperatorName() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.SCHEME_OPERATOR_NAME.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpeName(), detl.getSchemeInformation().getSchemeOpeName()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeOpeName(), detl.getSchemeInformation().getSchemeOpeName()));
    }

    @Test
    public void editPostalAddress() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.POSTAL_ADDRESSES.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpePostal(), detl.getSchemeInformation().getSchemeOpePostal()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeOpePostal(), detl.getSchemeInformation().getSchemeOpePostal()));
    }

    @Test
    public void editElectronicAddress() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.ELECTRONIC_ADDRESS.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeOpeElectronic(), detl.getSchemeInformation().getSchemeOpeElectronic()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeOpeElectronic(), detl.getSchemeInformation().getSchemeOpeElectronic()));
        assertFalse(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeOpeElectronic(), lotl.getSchemeInformation().getSchemeOpeElectronic()));
    }

    @Test
    public void editTslType() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.TSL_TYPE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getType().equalsIgnoreCase(detl.getSchemeInformation().getType()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getType().equalsIgnoreCase(detl.getSchemeInformation().getType()));
    }

    @Test
    public void editStatusDeterm() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.STATUS_DETERMINATION.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getStatusDetermination().equalsIgnoreCase(detl.getSchemeInformation().getStatusDetermination()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getStatusDetermination().equalsIgnoreCase(detl.getSchemeInformation().getStatusDetermination()));
    }

    @Test
    public void editSchemeTypeCommunity() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.SCHEME_TYPE_COMMUNITY_RULES.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeTypeCommRule(), detl.getSchemeInformation().getSchemeTypeCommRule()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeTypeCommRule(), detl.getSchemeInformation().getSchemeTypeCommRule()));
    }

    @Test
    public void editSchemeName() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.SCHEME_NAME.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeName(), detl.getSchemeInformation().getSchemeName()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeName(), detl.getSchemeInformation().getSchemeName()));
    }

    @Test
    public void editPolicyOrLegal() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.POLICY_OR_LEGAL_NOTICE.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemePolicy(), detl.getSchemeInformation().getSchemePolicy()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemePolicy(), detl.getSchemeInformation().getSchemePolicy()));
    }

    @Test
    public void editDistributionList() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.DISTRIBUTION_LIST.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getDistributionPoint(), detl.getSchemeInformation().getDistributionPoint()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getDistributionPoint(), detl.getSchemeInformation().getDistributionPoint()));
    }

    @Test
    public void editInformatioNUriList() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.SCHEME_INFORMATION_URI.toString(), "");
        // CHECK RETURN
        assertTrue(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeInfoUri(), detl.getSchemeInformation().getSchemeInfoUri()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeInfoUri(), detl.getSchemeInformation().getSchemeInfoUri()));
    }

    @Test
    public void editDefaultList() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        detl.getSchemeInformation().getSchemeInfoUri().get(0).setLanguage("CZ");

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.KEY_USAGE_BIT.toString(), "");
        // CHECK RETURN
        assertFalse(CollectionUtils.isEqualCollection(schemeInfoUpdated.getSchemeInfoUri(), detl.getSchemeInformation().getSchemeInfoUri()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertFalse(CollectionUtils.isEqualCollection(lotlUpdated.getSchemeInformation().getSchemeInfoUri(), detl.getSchemeInformation().getSchemeInfoUri()));
    }

}
