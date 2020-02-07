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
        rulesRunnerService.runAllRulesByTL(currentLOTL);
        tlValidator.checkLOTL(dbLOTL);
        auditService.addAuditLog(AuditTarget.PROD_TL, AuditAction.VALIDATION, AuditStatus.SUCCES, "EU", dbLOTL.getXmlFile().getId(), username, "Run LOTL Validation (signature/checks)");
    }
}
