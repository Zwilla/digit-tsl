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
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.constant.TLCCTarget;
import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLCCRequestDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLPointersToOtherTSL;
import eu.europa.ec.joinup.tsl.business.repository.ResultRepository;
import eu.europa.ec.joinup.tsl.business.rules.ComparisonCheckValidator;
import eu.europa.ec.joinup.tsl.business.rules.ListOfGenericsValidator;
import eu.europa.ec.joinup.tsl.business.rules.NotificationValidator;
import eu.europa.ec.joinup.tsl.business.rules.PointersToOtherTSLValidator;
import eu.europa.ec.joinup.tsl.business.rules.ServiceDigitalIdentityValidator;
import eu.europa.ec.joinup.tsl.business.rules.SieQValidator;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.DBCheckResult;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.CheckType;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Manage rules execution on all the part of the trusted list (all, scheme information, pointer, tsp, service, history, signature)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class RulesRunnerService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private CheckService checkService;

    @Autowired
    private ComparisonCheckValidator comparisonValidator;

    @Autowired
    private ListOfGenericsValidator genericValidator;

    @Autowired
    private TLCCService tlccService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TLTransitionCheckService transitionCheckService;

    @Autowired
    private NotificationValidator notificationValidator;

    @Autowired
    private PointersToOtherTSLValidator pointerValidator;

    @Autowired
    private ServiceDigitalIdentityValidator serviceDigitalIdentityValidator;

    @Autowired
    private SieQValidator sieQValidator;

    /**
     * Clean all results for current TL and run TLCC/Comparison and transition checks
     * 
     * @param tlId
     */
    public void runAllRulesByTLId(final int tlId) {
        TL draft = tlService.getTL(tlId);
        runAllRulesByTL(draft);
    }

    /**
     * Clean all results for current TL and run TLCC/Comparison and transition checks
     *
     * @param currentTL
     * @param previousTL
     */
    @Transactional(value = TxType.REQUIRES_NEW)
    public void runAllRulesByTL(final TL currentTL) {
        List<CheckResultDTO> checkResults = new ArrayList<>();
        if (currentTL != null) {
            checkService.deleteByTrustedListId(currentTL.getTlId());
            final List<CheckResultDTO> errorTlccChecks = tlccService.getErrorTlccChecks(new TLCCRequestDTO(currentTL.getTlId()), TLCCTarget.TRUSTED_LIST);
            if (errorTlccChecks != null) {
                checkResults.addAll(errorTlccChecks);
            }
            checkResults.addAll(transitionCheckService.getTransitionCheck(currentTL.getTlId()));
            checkResults.addAll(sieQValidator.getSieQCheck(currentTL));
            TL previousTL = tlService.getBaselineTL(currentTL);
            if (previousTL != null) {
                checkResults.addAll(getComparisonChecks(currentTL, previousTL));
            }
        }
        persistCheckResults(currentTL.getTlId(), checkResults);
    }

    public List<CheckResultDTO> validateNotification(TLPointersToOtherTSL pointer) {
        pointer.setTlId(tlService.getLOTLId());
        List<CheckResultDTO> results = new ArrayList<>();
        List<CheckDTO> checkList = checkService.getTarget(Tag.SCHEME_OPERATOR_NAME);
        genericValidator.runCheckOnGenerics(pointer.getId() + "_" + Tag.SCHEME_OPERATOR_NAME, checkList, pointer.getSchemeOpeName(), results);

        checkList = checkService.getTarget(Tag.POINTERS_TO_OTHER_TSL);
        pointerValidator.runValidation(pointer, checkList, results);

        if (CollectionUtils.isNotEmpty(pointer.getServiceDigitalId())) {
            checkList = checkService.getTarget(Tag.SERVICE_DIGITAL_IDENTITY);
            serviceDigitalIdentityValidator.runCheckDigitalIdentifications(checkList, pointer.getServiceDigitalId(), results);
        }

        // Notification checks
        results.addAll(notificationValidator.validateNotification(checkService.getTarget(Tag.NOTIFICATION), pointer));
        return results;
    }

    /**
     * Get comparison check result between current & previous TL
     *
     * @param current
     * @param previous
     * @return
     */
    private List<CheckResultDTO> getComparisonChecks(TL current, TL previous) {
        List<CheckResultDTO> results = new ArrayList<>();
        if (previous != null) {
            List<CheckDTO> checkList = checkService.getTarget(Tag.COMPARISON_CHECK);
            for (CheckDTO c : checkList) {
                results.addAll(comparisonValidator.validate(c, previous, current));
            }
        }
        return results;
    }

    /**
     * Persist CheckResultDTO list as DBCheckResult
     * 
     * @param checkResults
     */
    private void persistCheckResults(int tlId, List<CheckResultDTO> checkResults) {
        if (!CollectionUtils.isEmpty(checkResults)) {
            DBTrustedLists dbTL = tlService.getDbTL(tlId);
            List<DBCheckResult> dbChecks = new ArrayList<>();
            for (CheckResultDTO check : checkResults) {
                DBCheck dbCheck = checkService.getCheckById(check.getCheckId());
                DBCheckResult dbCheckResult = new DBCheckResult();
                dbCheckResult.setTrustedList(dbTL);
                dbCheckResult.setLocation(check.getId());
                dbCheckResult.setHrLocation(check.getLocation());
                dbCheckResult.setCheck(dbCheck);
                if (dbCheck.getType().equals(CheckType.TLCC)) {
                    dbCheckResult.setDescription(check.getDescription());
                    dbCheckResult.setStatus(dbCheck.getPriority());
                } else {
                    dbCheckResult.setStatus(check.getStatus());
                    dbCheckResult.setDescription(dbCheck.getDescription());
                }
                dbChecks.add(dbCheckResult);
            }
            resultRepository.save(dbChecks);
        }
    }

}
