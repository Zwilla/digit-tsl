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
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.model.DBFiles;
import eu.europa.ec.joinup.tsl.model.DBSignatureInformation;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Validate trusted list signature
 */
@Service
@Transactional(value = TxType.REQUIRED)
public class TLValidator {

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private SignersService signersService;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private XmlSignatureValidationService xmlSignatureValidationService;

    @Autowired
    private AuditService auditService;

    /**
     * Validate LOTL signature
     *
     * @param lotl
     */
    public void checkLOTL(DBTrustedLists lotl) {
        if (lotl != null) {
            lotl = tlRepository.findOne(lotl.getId());

            DBFiles xmlFile = lotl.getXmlFile();
            if ((xmlFile != null) && StringUtils.isNotEmpty(xmlFile.getLocalPath())) {
                DBSignatureInformation signatureInfo = xmlSignatureValidationService.validateLOTL(xmlFile);
                xmlFile.setSignatureInformation(signatureInfo);
                // Get signature status
                String status = "";
                if ((signatureInfo != null) && (signatureInfo.getIndication() != null)) {
                    status = "INDICATION:" + signatureInfo.getIndication() + ";";
                    if (signatureInfo.getSubIndication() != null) {
                        status += "SUB-INDICATION:" + signatureInfo.getSubIndication() + ";";
                    }
                }
                // Log audit
                if (lotl.getStatus().equals(TLStatus.DRAFT)) {
                    auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, lotl.getTerritory().getCodeTerritory(), lotl.getXmlFile().getId(), "SYSTEM",
                            "CLASS:TLVALIDATOR.CHECKLOTL,TLID:" + lotl.getId() + ",XMLFILEID:" + lotl.getXmlFile().getId() + status);
                } else {

                    auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, lotl.getTerritory().getCodeTerritory(), lotl.getXmlFile().getId(), "SYSTEM",
                            "CLASS:TLVALIDATOR.CHECKLOTL,TLID:" + lotl.getId() + ",XMLFILEID:" + lotl.getXmlFile().getId() + status);
                }
            }
        }
    }

    /**
     * Validate trusted list signature
     *
     * @param tl
     * @param potentialSignersForTL
     */
    public void checkTL(DBTrustedLists tl, List<CertificateToken> potentialSignersForTL) {
        tl = tlRepository.findOne(tl.getId());
        DBFiles xmlFile = tl.getXmlFile();
        if ((xmlFile != null) && StringUtils.isNotEmpty(xmlFile.getLocalPath())) {
            DBSignatureInformation signatureInfo = xmlSignatureValidationService.validateTL(xmlFile, potentialSignersForTL);
            if (tl.getStatus().equals(TLStatus.DRAFT)) {
                auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, tl.getTerritory().getCodeTerritory(), tl.getXmlFile().getId(), "SYSTEM",
                        "CLASS:TLVALIDATOR.CHECKTL,TLID:" + tl.getId() + ",XMLFILEID:" + tl.getXmlFile().getId());
            } else {
                auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, tl.getTerritory().getCodeTerritory(), tl.getXmlFile().getId(), "SYSTEM",
                        "CLASS:TLVALIDATOR.CHECKTL,TLID:" + tl.getId() + ",XMLFILEID:" + tl.getXmlFile().getId());
            }
            xmlFile.setSignatureInformation(signatureInfo);
        }

    }

    /**
     * Run DBChecks
     *
     * @param tl
     */
    public void checkTLorLOTLWithCurrentProdLOTL(DBTrustedLists tl) {
        Map<String, List<CertificateToken>> potentialsSigners = signersService.getAllPotentialsSigners();
        checkTL(tl, potentialsSigners.get(tl.getTerritory().getCodeTerritory()));
    }

    /**
     * Validate signature with DSS & run DBChecks
     *
     * @param tl
     */
    public void checkAllSignature(DBTrustedLists tl) {
        checkTLorLOTLWithCurrentProdLOTL(tl);
        rulesRunner.validateSignature(tl.getId());
    }

}
