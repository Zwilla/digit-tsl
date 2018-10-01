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

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

@Service
public class LOTLValidationService {

    @Autowired
    private TLService tlService;

    @Autowired
    private RulesRunnerService rulesRunnerService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private AuditService auditService;

    /**
     * Validate LOTL (Signature/Checks)
     */
    @Async
    @Transactional(value = TxType.REQUIRED)
    public void lotlValidation(String username) {
        DBTrustedLists dbLOTL = tlService.getLOTL();
        TL currentLOTL = tlService.getDtoTL(dbLOTL);
        TL previousLOTL = tlService.getPreviousProduction(dbLOTL.getTerritory());
        rulesRunnerService.runAllRules(currentLOTL, previousLOTL);
        tlValidator.checkLOTL(dbLOTL);
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.VALIDATION, AuditStatus.SUCCES, "EU", dbLOTL.getXmlFile().getId(), username,
                "Run LOTL Validation (signature/checks)");
    }
}
