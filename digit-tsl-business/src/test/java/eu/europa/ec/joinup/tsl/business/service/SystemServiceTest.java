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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.Column;
import eu.europa.ec.joinup.tsl.business.repository.ColumnsRepository;
import eu.europa.ec.joinup.tsl.model.DBColumnAvailable;
import eu.europa.ec.joinup.tsl.model.enums.ColumnName;

public class SystemServiceTest extends AbstractSpringTest {

	@Autowired
	private BackboneColumnsService systemService;

	@Autowired
	private ColumnsRepository columnsRepository;

	@Before
	public void init(){
		DBColumnAvailable cContact = new DBColumnAvailable();
		cContact.setCode(ColumnName.CONTACTS.toString());
		cContact.setVisible(true);

		DBColumnAvailable cAvailability = new DBColumnAvailable();
		cAvailability.setCode(ColumnName.AVAILABILITY.toString());
		cAvailability.setVisible(false);

		columnsRepository.save(cContact);
		columnsRepository.save(cAvailability);
	}

	@Test
	public void getColumns() {
		List<Column> columns = systemService.getColumns();
		Assert.assertNotNull(columns);
		Assert.assertEquals(3, columns.size());
	}

	@Test
	public void getColumnVisible() {
		Assert.assertTrue(systemService.getColumnVisible(ColumnName.CONTACTS.toString()));
		Assert.assertFalse(systemService.getColumnVisible(ColumnName.AVAILABILITY.toString()));
	}



}
