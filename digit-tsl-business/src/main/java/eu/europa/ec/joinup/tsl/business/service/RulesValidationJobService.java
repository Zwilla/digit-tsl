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

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.business.util.TLUtils;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;

/**
 * Validation job for rules
 */
@Service
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class RulesValidationJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesValidationJobService.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private AuditService auditService;

    public void start() {
        LOGGER.debug("**** START RULES VALIDATION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.CHECKCONFORMANCE, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start rules validation job");
        List<DBTrustedLists> notArchivedTLs = tlService.findTLNotArchived();

        if (CollectionUtils.isNotEmpty(notArchivedTLs)) {
            for (DBTrustedLists notArchivedTL : notArchivedTLs) {
                TL current = tlService.getTL(notArchivedTL.getId());
                if (current != null) {
                    LOGGER.debug("START run rule for " + current.getDbName() + " @ " + TLUtils.toStringFormat(new Date()));

                    rulesRunner.runAllRulesByTL(current);
                    if (TLStatus.DRAFT.equals(notArchivedTL.getStatus())) {
                        auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CHECKCONFORMANCE, AuditStatus.SUCCES, notArchivedTL.getTerritory().getCodeTerritory(),
                                notArchivedTL.getXmlFile().getId(), "SYSTEM", "CLASS:RulesValidationJobService.START,NAME:" + notArchivedTL.getName() + ",XMLFILEID:"
                                        + notArchivedTL.getXmlFile().getId() + ",XMLDIGEST:" + notArchivedTL.getXmlFile().getDigest());
                    } else {
                        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CHECKCONFORMANCE, AuditStatus.SUCCES, notArchivedTL.getTerritory().getCodeTerritory(),
                                notArchivedTL.getXmlFile().getId(), "SYSTEM", "CLASS:RulesValidationJobService.START,NAME:" + notArchivedTL.getName() + ",XMLFILEID:"
                                        + notArchivedTL.getXmlFile().getId() + ",XMLDIGEST:" + notArchivedTL.getXmlFile().getDigest());
                    }
                    tlService.setTlCheckStatus(current.getTlId());
                } else {
                    LOGGER.error("CURRENT TL IS NULL FOR " + notArchivedTL.getId() + " / " + notArchivedTL.getName());
                }
            }
        }
        LOGGER.debug("**** END RULES VALIDATION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.CHECKCONFORMANCE, AuditStatus.SUCCES, "", 0, "SYSTEM", "End rules validation job");
    }

}
