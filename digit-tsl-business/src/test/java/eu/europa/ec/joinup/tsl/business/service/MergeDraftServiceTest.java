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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.merge.MergeStatus;
import eu.europa.ec.joinup.tsl.business.dto.merge.TLMergeResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLName;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.MimeType;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MergeDraftServiceTest extends AbstractSpringTest {

    @Autowired
    private TLLoader tlLoader;

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TLService tlService;

    @Autowired
    private DraftStoreService draftstoreService;

    @Autowired
    private MergeDraftService mergeService;

    @Autowired
    private TrustedListJaxbService jaxbService;

    @Autowired
    private FileService fileService;

    @Autowired
    private TLRepository tlRepository;

    @Test
    public void AgetMergeChanges() throws IOException {
        List<TrustedListsReport> drafts = new ArrayList<>();

        Load loadTL = new Load();
        tlLoader.loadTL("BE", "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, loadTL);

        Load loadDraft = new Load();
        loadDraft.setNew(false);

        String newDraftStore = draftstoreService.getNewDraftStore();

        DBTrustedLists dbDraft1 = draftService.cloneTLtoDraft("BE", newDraftStore, "test", loadDraft);
        TL tlDraft = tlService.getDtoTL(dbDraft1);
        tlDraft.getSchemeInformation().getDistributionPoint().get(0).setValue("TEST");
        tlDraft.getSchemeInformation().setStatusDetermination("TEST");
        tlDraft.getPointers().get(0).getSchemeOpeName().get(0).setValue("TEST");
        tlDraft.getPointers().get(0).getServiceDigitalId().get(0).setSubjectName("TEST");
        tlDraft.getServiceProviders().get(0).getTSPPostal().get(0).setStreet("TEST");
        tlDraft.getServiceProviders().get(0).getTSPServices().get(0).setCurrentStatusStartingDate(new Date());
        tlDraft.getServiceProviders().get(0).getTSPServices().get(1).setCurrentStatus("TEST");
        tlDraft.getServiceProviders().get(0).getTSPServices().get(1).setTypeIdentifier("TEST");
        persistAndAddDraft(drafts, dbDraft1, tlDraft);

        loadDraft = new Load();
        loadDraft.setNew(false);
        DBTrustedLists dbDraft2 = draftService.cloneTLtoDraft("BE", newDraftStore, "test", loadDraft);
        TL tlDraft2 = tlService.getDtoTL(dbDraft2);
        tlDraft2.getSchemeInformation().setStatusDetermination("TEST");
        tlDraft2.getSchemeInformation().getSchemeTypeCommRule().get(0).setValue("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPPostal().get(0).setStreet("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setCurrentStatusStartingDate(new Date());
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setCurrentStatus("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setTypeIdentifier("TEST");
        tlDraft2.getServiceProviders().get(0).getTSPServices().get(2).setCurrentStatus("TEST");
        persistAndAddDraft(drafts, dbDraft2, tlDraft2);

        TLMergeResultDTO mergeResult = mergeService.getTLMergeResult("BE", drafts);
        Assert.assertNotNull(mergeResult);

        Assert.assertEquals(3, mergeResult.getSiResult().getMergeResult().size());
        Assert.assertEquals(1, mergeResult.getPointerResult().getMergeResult().size());
        Assert.assertEquals(2, mergeResult.getTspResult().getMergeResult().size());

        Assert.assertEquals(2, mergeResult.getSiResult().getMergeResult().get("Status Determination Approach").size());
        Assert.assertTrue(mergeResult.getPointerResult().getMergeResult().containsKey("XML - EU"));
        Assert.assertEquals(2, mergeResult.getTspResult().getMergeResult().get("Certipost n.v./s.a.").size());
    }

    @Test
    public void BtestNewAndDelete() throws IOException {
        List<TrustedListsReport> drafts = new ArrayList<>();

        Load loadTL = new Load();
        tlLoader.loadTL("BE", "https://www.signatur.rtr.at/currenttl.xml", TLType.TL, TLStatus.PROD, loadTL);

        Load loadDraft = new Load();
        loadDraft.setNew(false);

        String newDraftStore = draftstoreService.getNewDraftStore();
        DBTrustedLists dbDraft = draftService.cloneTLtoDraft("BE", newDraftStore, "test", loadDraft);
        TL tlDraft = tlService.getDtoTL(dbDraft);
        tlDraft.getPointers().get(0).setMimeType(MimeType.PDF);
        tlDraft.getPointers().get(0).setSchemeTerritory("FR");
        tlDraft.getServiceProviders().set(4, tlDraft.getServiceProviders().get(6));
        tlDraft.getServiceProviders().set(5, null);
        tlDraft.getServiceProviders().set(6, null);
        persistAndAddDraft(drafts, dbDraft, tlDraft);

        TLMergeResultDTO mergeResult = mergeService.getTLMergeResult("BE", drafts);
        Assert.assertNotNull(mergeResult);
        Assert.assertEquals(2, mergeResult.getTspResult().getMergeResult().size());
        Assert.assertEquals(MergeStatus.DELETE, mergeResult.getPointerResult().getMergeResult().get("XML - EU").get(0).getStatus());
        Assert.assertEquals(MergeStatus.NEW, mergeResult.getPointerResult().getMergeResult().get("PDF - FR").get(0).getStatus());
        Assert.assertEquals(MergeStatus.DELETE, mergeResult.getTspResult().getMergeResult().get("Portima s.c.r.l. c.v.b.a.").get(0).getStatus());
        Assert.assertEquals(MergeStatus.DELETE, mergeResult.getTspResult().getMergeResult().get("Connect Solutions").get(0).getStatus());
    }

    @Test
    public void CmergeDrafts() throws IOException {
        List<TrustedListsReport> drafts = new ArrayList<>();

        Load loadTL = new Load();
        String cc = "FR";
        tlLoader.loadTL(cc, " http://www.ssi.gouv.fr/eidas/TL-FR.xml", TLType.TL, TLStatus.PROD, loadTL);

        Load loadDraft = new Load();
        loadDraft.setNew(false);

        String newDraftStore = draftstoreService.getNewDraftStore();

        DBTrustedLists dbDraft1 = draftService.cloneTLtoDraft(cc, newDraftStore, "test", loadDraft);
        TL tlDraft = tlService.getDtoTL(dbDraft1);
        tlDraft.getSchemeInformation().getSchemeOpeName().get(0).setValue("TEST");
        tlDraft.getSchemeInformation().setStatusDetermination("TEST");
        tlDraft.getPointers().get(0).getSchemeOpeName().get(0).setValue("TEST");
        tlDraft.getPointers().get(0).getServiceDigitalId().get(0).setSubjectName("TEST");
        tlDraft.getServiceProviders().get(0).getTSPPostal().get(0).setStreet("TEST");
        tlDraft.getServiceProviders().get(0).getTSPServices().get(0).setCurrentStatusStartingDate(new Date());
        tlDraft.getServiceProviders().get(0).getTSPServices().get(1).setCurrentStatus("TEST");
        tlDraft.getServiceProviders().get(0).getTSPServices().get(1).setTypeIdentifier("TEST");
        persistAndAddDraft(drafts, dbDraft1, tlDraft);

        loadDraft = new Load();
        loadDraft.setNew(false);

        DBTrustedLists dbDraft2 = draftService.cloneTLtoDraft(cc, newDraftStore, "test", loadDraft);
        TL tlDraft2 = tlService.getDtoTL(dbDraft2);
        tlDraft2.getSchemeInformation().getSchemeOpeElectronic().get(0).setValue("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPPostal().get(0).setStreet("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setCurrentStatusStartingDate(new Date());
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setCurrentStatus("TEST");
        tlDraft2.getServiceProviders().get(1).getTSPServices().get(0).setTypeIdentifier("TEST");
        tlDraft2.getSchemeInformation().getSchemePolicy().get(0).setValue("TEST");
        tlDraft2.getServiceProviders().get(3).getTSPServices().get(0).setCurrentStatus("TEST");
        TLPointersToOtherTSL ptotl = new TLPointersToOtherTSL();
        ptotl.setMimeType(MimeType.PDF);
        List<TLName> schemeOpeName = new ArrayList<>();
        TLName name = new TLName();
        name.setLanguage("en");
        name.setValue("TEST");
        schemeOpeName.add(name);
        ptotl.setSchemeOpeName(schemeOpeName);
        ptotl.setSchemeTerritory(cc);
        ptotl.setTlLocation("TEST-location");
        ptotl.setTlType("TEST-type");
        tlDraft2.getPointers().add(ptotl);
        persistAndAddDraft(drafts, dbDraft2, tlDraft2);

        TLMergeResultDTO changesFromDrafts = mergeService.getTLMergeResult(cc, drafts);
        Assert.assertNotNull(changesFromDrafts);
        mergeService.mergeDrafts(cc, newDraftStore, changesFromDrafts);
    }

    @Test
    public void Eclean() throws IOException {
        // No check but use of @After results to tests failure
        FileUtils.deleteDirectory(new File("src/test/resources/tsl/BE"));
    }

    private void persistAndAddDraft(List<TrustedListsReport> drafts, DBTrustedLists dbDraft, TL tlDraft) {

        byte[] marshallToBytesAsV5 = jaxbService.marshallToBytesAsV5(tlDraft);
        DBFiles xmlFile = dbDraft.getXmlFile();
        xmlFile.setLocalPath(fileService.storeNewDraftTL(xmlFile.getMimeTypeFile(), marshallToBytesAsV5, dbDraft.getTerritory().getCodeTerritory(), xmlFile.getLocalPath()));
        tlRepository.save(dbDraft);
        TrustedListsReport draft = new TrustedListsReport();
        draft.setId(dbDraft.getId());
        drafts.add(draft);
    }
}
