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

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.repository.CheckRepository;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckName;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Transactional
public class ChecksServiceTest extends AbstractSpringTest {

    @Autowired
    private CheckService checksService;

    @Autowired
    private CheckRepository checkRepository;

    @Before
    public void init() {
        checkRepository.deleteAll();
        createCheckTest();
    }

    private void createCheckTest() {
        DBCheck cError = new DBCheck();
        cError.setId("TEST.test");
        cError.setImpact(CheckImpact.TRUSTBACKBONE);
        cError.setName(CheckName.IS_URI);
        cError.setPriority(CheckStatus.ERROR);
        cError.setTarget(Tag.TSP_SERVICE_DEFINITION_URI);
        cError.setDescription("test");

        checkRepository.save(cError);
    }

    @Test
    public void getTarget() {
        List<CheckDTO> checks = checksService.getTarget(Tag.TSP_SERVICE_DEFINITION_URI);
        Assert.assertNotNull(checks);
        Assert.assertEquals(1, checks.size());
    }

    @Test
    public void edit() {
        CheckDTO check = checksService.getAll().get(0);
        Assert.assertEquals(CheckStatus.ERROR, check.getStatus());
        check.setStatus(CheckStatus.IGNORE);
        checksService.edit(check);
        CheckDTO checkEdit = checksService.getAll().get(0);
        Assert.assertEquals(CheckStatus.IGNORE, checkEdit.getStatus());
    }

}
