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
package eu.europa.ec.joinup.tsl.business.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class TLRepositoryTest extends AbstractSpringTest {

	@Autowired
	private TLRepository repo;

	@Autowired
	private CountryRepository countryRepository;

	@Test
	public void findByTerritoryAndStatusAndArchiveTrueOrderByIssueDateDesc() {
		DBCountries territory = createEurope();

		int createTLinDB = createTLinDB(TLStatus.PROD, false, territory);
		assertTrue(createTLinDB > 0);

		DBTrustedLists findOne = repo.findOne(createTLinDB);
		assertNotNull(findOne);
		assertNotNull(findOne.getTerritory());
		assertEquals(territory.getCodeTerritory(), findOne.getTerritory().getCodeTerritory());

		assertTrue(CollectionUtils.isEmpty(repo.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD)));

		createTLinDB = createTLinDB(TLStatus.PROD, true, territory);
		assertTrue(createTLinDB > 0);

		assertTrue(CollectionUtils.isNotEmpty(repo.findByTerritoryAndStatusAndArchiveTrueOrderByIdDesc(territory, TLStatus.PROD)));

	}

	private DBCountries createEurope() {
		DBCountries territory = new DBCountries();
		territory.setCodeTerritory("EU");
		territory.setCountryName("europa");
		return countryRepository.save(territory);
	}

	private int createTLinDB(TLStatus status, boolean archive, DBCountries country) {
		DBTrustedLists trustedList = new DBTrustedLists();
		trustedList.setType(TLType.LOTL);
		trustedList.setTerritory(country);
		trustedList.setXmlFile(new DBFiles());
		trustedList.setStatus(status);
		trustedList.setArchive(archive);
		repo.save(trustedList);
		return trustedList.getId();
	}
}
