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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europa.ec.joinup.tsl.business.AbstractSpringTest;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.DraftStoreRetentionDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionTarget;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.TrustedListRetentionDTO;
import eu.europa.ec.joinup.tsl.business.repository.CountryRepository;
import eu.europa.ec.joinup.tsl.business.repository.DraftStoreRepository;
import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBDraftStore;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;

public class RetentionServiceTest extends AbstractSpringTest {

    @Autowired
    private RetentionService retentionService;

    @Autowired
    private RetentionJobService retentionJob;

    @Autowired
    private DraftStoreService dsService;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private DraftStoreRepository dsRepository;

    @Autowired
    private CountryRepository countryRepository;

    private String dsID;

    @Before
    public void init() {
        tlRepository.deleteAll();
        dsRepository.deleteAll();
        Assert.assertTrue(IterableUtils.isEmpty(tlRepository.findAll()));
        Assert.assertTrue(IterableUtils.isEmpty(dsRepository.findAll()));

        dsID = dsService.getNewDraftStore();
        dsService.getNewDraftStore();

        DBCountries country = countryRepository.findOne("BE");

        Date lastAccessDate = new Date();
        DBTrustedLists dbTL = saveTL(country, TLStatus.PROD, "TEST 1", lastAccessDate);

        DBDraftStore dbDS = dsService.findOneInitialized(dsID);
        dbDS.getDraftList().add(dbTL);
    }

    @Test
    public void getCronResult() {
        RetentionCriteriaDTO criteriaDTO = new RetentionCriteriaDTO();
        criteriaDTO.setTarget(RetentionTarget.DRAFTSTORE);
        List<DraftStoreRetentionDTO> dsRetention = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertTrue(dsRetention.isEmpty());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.DAY_OF_MONTH, -10);
        Date oldDate = cal.getTime();
        DBDraftStore dbDS = dsService.findOne(dsID);
        dbDS.setLastVerification(oldDate);
        dsRepository.save(dbDS);

        List<DraftStoreRetentionDTO> dsRetention2 = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertTrue(dsRetention2.isEmpty());

        setDraftstorePastDate();

        List<DraftStoreRetentionDTO> dsRetention3 = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertFalse(dsRetention3.isEmpty());
        Assert.assertEquals(1, dsRetention3.get(0).getTls().size());
    }

    @Test
    public void cleanDraftstore() {
        RetentionCriteriaDTO criteriaDTO = new RetentionCriteriaDTO();
        criteriaDTO.setTarget(RetentionTarget.DRAFTSTORE);

        setDraftstorePastDate();

        List<DraftStoreRetentionDTO> dsRetention = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertFalse(dsRetention.isEmpty());
        Assert.assertEquals(1, dsRetention.get(0).getTls().size());

        for (DraftStoreRetentionDTO tmp : dsRetention) {
            retentionService.cleanDraftStore(tmp);
        }

        Assert.assertTrue(IterableUtils.isEmpty(tlRepository.findAll()));
        Assert.assertEquals(1, IterableUtils.size(dsRepository.findAll()));

    }

    @Test
    public void cleanTL() {
        RetentionCriteriaDTO criteriaDTO = new RetentionCriteriaDTO();
        criteriaDTO.setTarget(RetentionTarget.DRAFT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        saveTL(countryRepository.findOne("DE"), TLStatus.DRAFT, "TEST DE", cal.getTime());
        Assert.assertEquals(2, IterableUtils.size(tlRepository.findAll()));

        List<DraftStoreRetentionDTO> dsRetention = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertFalse(dsRetention.isEmpty());
        Assert.assertEquals(1, dsRetention.get(0).getTls().size());

        for (TrustedListRetentionDTO tmp : dsRetention.get(0).getTls()) {
            retentionService.cleanTrustedlist(tmp);
        }

        Assert.assertEquals(1, IterableUtils.size(tlRepository.findAll()));

    }

    @Test
    public void retentionJob() {
        Assert.assertEquals(1, IterableUtils.size(tlRepository.findAll()));
        Assert.assertEquals(2, IterableUtils.size(dsRepository.findAll()));

        RetentionCriteriaDTO criteriaDTO = new RetentionCriteriaDTO();
        criteriaDTO.setTarget(RetentionTarget.DRAFTSTORE);

        setDraftstorePastDate();

        List<DraftStoreRetentionDTO> dsRetention = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertFalse(dsRetention.isEmpty());
        Assert.assertEquals(1, dsRetention.get(0).getTls().size());

        // New draft
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        saveTL(countryRepository.findOne("DE"), TLStatus.DRAFT, "TEST DE", cal.getTime());
        Assert.assertEquals(2, IterableUtils.size(tlRepository.findAll()));

        criteriaDTO.setTarget(RetentionTarget.DRAFT);
        dsRetention = retentionService.searchRetentionData(criteriaDTO);
        Assert.assertEquals(1, dsRetention.get(0).getTls().size());

        retentionJob.start(false);

        Assert.assertTrue(IterableUtils.isEmpty(tlRepository.findAll()));
        Assert.assertEquals(1, IterableUtils.size(dsRepository.findAll()));

    }

    @Test(expected = IllegalArgumentException.class)
    public void searchException() {
        retentionService.searchRetentionData(new RetentionCriteriaDTO());
    }

    @Test
    public void searchAndClean() {
        RetentionCriteriaDTO retentionCriteria = new RetentionCriteriaDTO();
        retentionCriteria.setTarget(RetentionTarget.DRAFTSTORE);
        Assert.assertTrue(CollectionUtils.isEmpty(retentionService.searchRetentionData(retentionCriteria)));

        setDraftstorePastDate();
        Assert.assertEquals(1, retentionService.searchRetentionData(retentionCriteria).size());

        retentionCriteria.setTarget(RetentionTarget.PRODUCTION);
        Assert.assertEquals(1, retentionService.searchRetentionData(retentionCriteria).size());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        Date twoMonthAgo = cal.getTime();
        retentionCriteria.setDate(twoMonthAgo);
        List<DraftStoreRetentionDTO> result = retentionService.searchRetentionData(retentionCriteria);
        Assert.assertEquals(RetentionTarget.PRODUCTION.toString(), result.get(0).getDraftStoreId());
        Assert.assertEquals(0, result.get(0).getTls().size());

    }

    private DBTrustedLists saveTL(DBCountries country, TLStatus status, String name, Date lastAccessDate) {
        DBTrustedLists dbTL = new DBTrustedLists();
        dbTL.setName(name);
        dbTL.setTerritory(country);
        dbTL.setXmlFile(new DBFiles());
        dbTL.setType(TLType.TL);
        dbTL.setStatus(status);
        dbTL.setSequenceNumber(100);
        dbTL.setVersionIdentifier(5);
        dbTL.setIssueDate(new Date());
        dbTL.setNextUpdateDate(new Date());
        dbTL.setArchive(false);
        dbTL.setLastAccessDate(lastAccessDate);
        dbTL.setLastEditedDate(new Date());
        dbTL.setDraftStoreId(dsID);
        tlRepository.save(dbTL);
        return dbTL;
    }

    private void setDraftstorePastDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date oldDate2 = cal.getTime();
        DBDraftStore dbDS = dsService.findOne(dsID);
        dbDS.setLastVerification(oldDate2);
        dsRepository.save(dbDS);
    }
}
