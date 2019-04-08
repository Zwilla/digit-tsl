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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.repository.CheckRepository;
import eu.europa.ec.joinup.tsl.business.repository.ResultRepository;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Service
@Transactional(value = TxType.REQUIRED)
public class CheckService {

    @Autowired
    private CheckRepository checkRepository;

    @Autowired
    private ResultRepository resultRepository;

    private static final List<CheckStatus> errorStatus = new ArrayList<>(Arrays.asList(CheckStatus.ERROR, CheckStatus.WARNING, CheckStatus.INFO));

    /**
     * Get all database checks and store in cache
     */
    @Cacheable(value = "checkCache", key = "#root.methodName")
    public List<CheckDTO> getAll() {
        List<CheckDTO> list = new ArrayList<>();
        List<DBCheck> dbList = checkRepository.findAllByOrderByDescription();
        for (DBCheck dbCheck : dbList) {
            list.add(new CheckDTO(dbCheck));
        }
        return list;
    }

    /**
     * Get DBDCheck by ID
     * 
     * @param id
     */
    public DBCheck getCheckById(String id) {
        return checkRepository.findOne(id);
    }

    /**
     * Get check by @Target and stored in cache
     *
     * @param target
     */
    @Cacheable(value = "checkCache", key = "#target")
    public List<CheckDTO> getTarget(Tag target) {
        List<CheckDTO> list = new ArrayList<>();
        List<DBCheck> dbList = checkRepository.findByTarget(target);
        for (DBCheck dbCheck : dbList) {
            list.add(new CheckDTO(dbCheck));
        }
        return list;
    }

    /**
     * Edit database check
     *
     * @param check
     */
    public DBCheck edit(CheckDTO check) {
        DBCheck checkToEdit = checkRepository.findOne(check.getId());
        checkToEdit.setPriority(check.getStatus());
        return checkRepository.save(checkToEdit);
    }

    /**
     * Delete check results by @DBCheck
     *
     * @param check
     */
    public void deleteCheckResult(DBCheck check) {
        List<DBCheckResult> resultList = resultRepository.findByCheck(check);
        resultRepository.delete(resultList);
    }

    /**
     * Clean database check by location starting by @location
     *
     * @param location
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void cleanResultByLocation(String location) {
        // Only for DRAFT
        List<DBCheckResult> dbList = resultRepository.findByLocationStartingWith(location);
        if (CollectionUtils.isNotEmpty(dbList)) {
            if (dbList.get(0).getTrustedList().getStatus().equals(TLStatus.DRAFT)) {
                resultRepository.delete(dbList);
            }
        }
    }

    /**
     * Clean comparison checks
     * 
     * @param tlId
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void cleanComparisonCheck(int tlId) {
        resultRepository.deleteByTrustedListIdAndCheckTarget(tlId, Tag.COMPARISON_CHECK);
    }

    /**
     * Clean transition checks
     * 
     * @param tlId
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void cleanTransitionCheck(int tlId) {
        resultRepository.deleteByTrustedListIdAndCheckTarget(tlId, Tag.TRANSITION_CHECK);
    }

    /**
     * @param id
     * @param onlyError
     * @param allResult
     *            - if false, get only 'current' result (No historic)
     * @return
     */
    public List<CheckDTO> getTLChecksResult(int id) {
        List<CheckDTO> results = new ArrayList<>();
        List<DBCheckResult> dbCheckResultList = resultRepository.findByTrustedListIdAndStatusInAndEndDateIsNull(id, errorStatus);
        for (DBCheckResult dbResult : dbCheckResultList) {
            results.add(new CheckDTO(dbResult));
        }
        return results;
    }

    /**
     * Create DBCheck map.
     *
     * @return Map<CheckID, DBCheck>
     */
    public Map<String, DBCheck> getCheckMap() {
        return initCheckMap(checkRepository.findAll());
    }

    /**
     * Create DBCheck map by tag
     *
     * @return Map<CheckID, DBCheck>
     */
    public Map<String, DBCheck> getCheckMapByType(Tag tag) {
        return initCheckMap(checkRepository.findByTarget(tag));
    }

    private Map<String, DBCheck> initCheckMap(List<DBCheck> tlccDBCheck) {
        Map<String, DBCheck> checkMap = new HashMap<>();
        if (tlccDBCheck != null) {
            for (DBCheck check : tlccDBCheck) {
                checkMap.put(check.getId(), check);
            }
        }
        return checkMap;
    }

}
