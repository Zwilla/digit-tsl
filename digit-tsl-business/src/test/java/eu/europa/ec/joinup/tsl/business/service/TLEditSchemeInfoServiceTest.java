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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(schemeInfoUpdated.getSequenceNumber() == SN);

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        // CHECK DB
        assertTrue(dbLOTL.getSequenceNumber() == SN);

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getSequenceNumber() == SN);
    }

    @Test
    public void editIssueDate() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.ISSUE_DATE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getIssueDate().equals(detl.getSchemeInformation().getIssueDate()));

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        DBTrustedLists dbDE = tlService.getDbTL(2);
        // CHECK DB
        assertTrue(dbLOTL.getIssueDate().equals(dbDE.getIssueDate()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getIssueDate().equals(detl.getSchemeInformation().getIssueDate()));
    }

    @Test
    public void editNextDate() {
        TL lotl = tlService.getTL(1);
        TL detl = tlService.getTL(2);
        assertTrue(lotl.getSchemeInformation().getTerritory().equalsIgnoreCase(EU_COUNTRY_CODE));
        assertTrue(detl.getSchemeInformation().getTerritory().equalsIgnoreCase(BE_COUNTRY_CODE));

        TLSchemeInformation schemeInfoUpdated = tlEditSchemeInfoService.edit(lotl.getTlId(), detl.getSchemeInformation(), Tag.NEXT_UPDATE.toString(), "");
        // CHECK RETURN
        assertTrue(schemeInfoUpdated.getNextUpdateDate().equals(detl.getSchemeInformation().getNextUpdateDate()));

        DBTrustedLists dbLOTL = tlService.getDbTL(1);
        DBTrustedLists dbDE = tlService.getDbTL(2);
        // CHECK DB
        assertTrue(dbLOTL.getNextUpdateDate().equals(dbDE.getNextUpdateDate()));

        TL lotlUpdated = tlService.getTL(1);
        // CHECK XML FILE
        assertTrue(lotlUpdated.getSchemeInformation().getNextUpdateDate().equals(detl.getSchemeInformation().getNextUpdateDate()));
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
