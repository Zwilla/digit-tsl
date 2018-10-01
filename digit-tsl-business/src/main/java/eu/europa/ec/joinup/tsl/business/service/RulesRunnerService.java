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
import eu.europa.ec.joinup.tsl.business.dto.tl.TLServiceProvider;
import eu.europa.ec.joinup.tsl.business.rules.ComparisonCheckValidator;
import eu.europa.ec.joinup.tsl.business.rules.ListOfGenericsValidator;
import eu.europa.ec.joinup.tsl.business.rules.NotificationValidator;
import eu.europa.ec.joinup.tsl.business.rules.PointersToOtherTSLValidator;
import eu.europa.ec.joinup.tsl.business.rules.ServiceDigitalIdentityValidator;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * Manage rules execution on all the part of the trusted list (all, scheme information, pointer, tsp, service, history, signature)
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class RulesRunnerService {

    @Autowired
    private TLCCService tlccService;

    @Autowired
    private CheckService checkService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckResultPersistenceService persistenceService;

    @Autowired
    private ComparisonCheckValidator comparisonValidator;

    @Autowired
    private PointersToOtherTSLValidator pointerValidator;

    @Autowired
    private NotificationValidator notificationValidator;

    @Autowired
    private ServiceDigitalIdentityValidator serviceDigitalIdentityValidator;

    @Autowired
    private ListOfGenericsValidator genericValidator;

    /**
     * Run All rules; perform TLCC and TLM checks
     *
     * @param currentTL
     * @param previousTL
     */
    public void runAllRules(final TL currentTL, final TL previousTL) {
        if (currentTL != null) {
            List<CheckResultDTO> checkResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(currentTL.getTlId()), TLCCTarget.TRUSTED_LIST);
            if (checkResults == null) {
                //Case where TLCC unreachable
                checkResults = new ArrayList<>();
            }
            checkResults.addAll(getComparisonChecks(currentTL, previousTL));
            persistenceService.persistAllResults(currentTL.getTlId(), checkResults);
        }
    }

    /**
     * Clean checks & validate scheme information. Perform TLCC API on @TLCCTarget.SCHEME_INFORMATION
     *
     * @param tlId
     */
    public void validateSchemeInformation(int tlId) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.SCHEME_INFORMATION);
        List<CheckResultDTO> tlccCheckResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId), TLCCTarget.SCHEME_INFORMATION);
        persistenceService.persistAllResults(tlId, tlccCheckResults);
    }

    /**
     * Clean checks & validate all PTOTSL. Perform TLCC API on @TLCCTarget.POINTERS_TO_OTHER_TSL
     *
     * @param tlId
     */
    public void validateAllPointers(int tlId) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.POINTERS_TO_OTHER_TSL);
        List<CheckResultDTO> tlccCheckResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId), TLCCTarget.POINTERS_TO_OTHER_TSL);
        persistenceService.persistAllResults(tlId, tlccCheckResults);
    }

    /**
     * Clean checks & validate all TSP. Look through TSP and perform TLCC API on @TLCCTarget.TSP_SERVICE_PROVIDER
     *
     * @param tlId
     * @param serviceProviders
     */
    public void validateAllServiceProvider(int tlId, List<TLServiceProvider> serviceProviders) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.TSP_SERVICE_PROVIDER);
        if (!CollectionUtils.isEmpty(serviceProviders)) {
            //Index start at 1 for TLCC Index result
            for (int index = 1; index < (serviceProviders.size() + 1); index++) {
                validateServiceProvider(tlId, index, true);
            }
        }
    }

    /**
     * Clean checks if not already cleaned & valide TSP. Perform TLCC API on @TLCCTarget.TSP_SERVICE_PROVIDER
     *
     * @param tlId
     * @param tspIndex
     * @param resultClean
     *            : true when called through validateAllServiceProvider. Rules are cleaned in parent method
     */
    public void validateServiceProvider(int tlId, int tspIndex, boolean resultClean) {
        if (!resultClean) {
            checkService.cleanResultByLocation(tlId + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + tspIndex);
        }
        List<CheckResultDTO> tlccCheckResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId, tspIndex), TLCCTarget.TRUST_SERVICE_PROVIDER);
        persistenceService.persistAllResults(tlId, tlccCheckResults);

    }

    /**
     * Clean checks & validate a service. Get specifc service by @tspIndex & @serviceIndex and perform TLCC API on @TLCCTarget.TSP_SERVICE
     *
     * @param tlId
     * @param tspIndex
     * @param serviceIndex
     */
    public void validateService(int tlId, int tspIndex, int serviceIndex) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.TSP_SERVICE_PROVIDER + "_" + tspIndex + "_" + Tag.TSP_SERVICE + "_" + serviceIndex);
        List<CheckResultDTO> tlccCheckResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId, tspIndex, serviceIndex), TLCCTarget.TSP_SERVICE);
        persistenceService.persistAllResults(tlId, tlccCheckResults);
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

        //Notification checks
        results.addAll(notificationValidator.validateNotification(checkService.getTarget(Tag.NOTIFICATION), pointer));
        return results;
    }

    /**
     * Run comparison check on trusted list.
     *
     * @param current
     * @param previous
     */
    public void compareTL(TL current, TL previous) {
        List<CheckResultDTO> results = getComparisonChecks(current, previous);
        persistenceService.persistAllResults(current.getTlId(), results);
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

    public void validateSignature(int tlId) {
        checkService.cleanResultByLocation(tlId + "_" + Tag.SIGNATURE);
        TL tl = tlService.getTL(tlId);
        //TODO(5.4.RC1) TDEV-794: Temporary solution. Waiting for a specific TLCC API to check Signature only
        //In the futur version of TLCC, check should be performed with the following line
        //tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId), TLCCTarget.SIGNATURE);
        if ((tl.getSigStatus() != null)) {
            List<CheckResultDTO> tlccCheckResults = tlccService.getErrorTlccChecks(new TLCCRequestDTO(tlId), TLCCTarget.TRUSTED_LIST);
            if (!CollectionUtils.isEmpty(tlccCheckResults)) {
                List<CheckResultDTO> signatureCheckResults = new ArrayList<>();
                for (CheckResultDTO tlccCheck : tlccCheckResults) {
                    if (tlccCheck.getCheckId().contains(Tag.SignatureRules.toString())) {
                        signatureCheckResults.add(tlccCheck);
                    }
                }
                persistenceService.persistAllResults(tlId, signatureCheckResults);
            }
        }
    }

    /**
     * Run all checks after signature performed on draft
     *
     * @param tlId
     */
    public void validDraftAfterSignature(int tlId) {
        checkService.cleanResultByLocation(tlId + "_");
        TL draft = tlService.getTL(tlId);
        TL currentProd = tlService.getPublishedTLByCountryCode(draft.getSchemeInformation().getTerritory());
        runAllRules(draft, currentProd);
    }

}
