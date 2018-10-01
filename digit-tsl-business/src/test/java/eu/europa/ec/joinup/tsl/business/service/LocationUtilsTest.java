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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.MigrationSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class LocationUtilsTest extends MigrationSpringTest {

	//private static final String COUNTRY_CODE = "DE";
	private static final String COUNTRY_CODE = "BE";

	@Autowired
	private TLService tlService;

	@Autowired
	private TLLoader tlLoader;

	@Autowired
	private TLDraftService draftService;

	@Autowired
	private DraftStoreService draftStoreService;

	@Test
	public void locationDif() throws FileNotFoundException, IOException {
		Load load = new Load();
		//tlLoader.loadTL(COUNTRY_CODE, "https://www.nrca-ds.de/st/TSL-XML.xml", "pdfUrl", "sha2Url", TLType.TL, TLStatus.PROD, load);
		tlLoader.loadTL(COUNTRY_CODE, "https://tsl.belgium.be/tsl-be.xml", TLType.TL, TLStatus.PROD, load);

		List<TrustedListsReport> listProd = tlService.getAllProdTlReports();
		System.out.println("********* " + listProd.get(0).getId());
		TL published = tlService.getTL(listProd.get(0).getId());
		assertNotNull(published.getSchemeInformation().getTerritory());

		byte[] byteArray = IOUtils.toByteArray(new FileInputStream("src/test/resources/tsl/BE/2016-10-13_12-55-38.xml"));
		DBTrustedLists draftTSL = draftService.createDraftFromXML(byteArray, draftStoreService.getNewDraftStore(),"test-man", new Load());
		assertNotNull(draftTSL);
		//assertEquals("DE", draftTSL.getTerritory().getCodeTerritory());
		assertEquals("BE", draftTSL.getTerritory().getCodeTerritory());

		TL draft = tlService.getTL(draftTSL.getId());
		assertNotNull(draft.getSchemeInformation().getTerritory());

		assertEquals(draft.getSchemeInformation().getTerritory(), published.getSchemeInformation().getTerritory());

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeInfoUri())) {
			draft.getSchemeInformation().getSchemeInfoUri().get(0).setValue("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getDistributionPoint())) {
			draft.getSchemeInformation().getDistributionPoint().get(0).setValue("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeName())) {
			draft.getSchemeInformation().getSchemeName().get(0).setValue("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeOpeElectronic())) {
			draft.getSchemeInformation().getSchemeOpeElectronic().get(0).setValue("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeOpeName())) {
			draft.getSchemeInformation().getSchemeOpeName().get(0).setValue("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeOpePostal())) {
			draft.getSchemeInformation().getSchemeOpePostal().get(0).setPostalCode("toto");
		}

		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemePolicy())) {
			draft.getSchemeInformation().getSchemePolicy().get(0).setValue("toto");
		}
		if (CollectionUtils.isNotEmpty(draft.getSchemeInformation().getSchemeTypeCommRule())) {
			draft.getSchemeInformation().getSchemeTypeCommRule().get(0).setValue("toto");
		}

		List<TLDifference> difList = draft.asPublishedDiff(published);
		assertNotNull(difList);
		for (TLDifference dif : difList) {
			System.out.println(dif.getId() + " / " + dif.getHrLocation());
		}

	}

}
