package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.TLStatus;
import eu.europa.ec.joinup.tsl.model.enums.TLType;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * Validation job for signature
 */
@Service
public class SignatureValidationJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureValidationJobService.class);

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private SignersService signersService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private CountryService countryService;

    public void start() {
        LOGGER.debug("**** START SIGNATURE VALIDATION " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start signature validation job");
        DBTrustedLists lotl = tlRepository.findByTerritoryAndStatusAndArchiveFalse(countryService.getLOTLCountry(), TLStatus.PROD);
        if (lotl != null) {
            Map<String, List<CertificateToken>> potentialSigners = signersService.getTLPotentialsSigners();

            List<DBTrustedLists> unarchivedTLs = tlRepository.findAllByArchiveFalseOrderByNameAsc();
            if (CollectionUtils.isNotEmpty(unarchivedTLs)) {
                for (DBTrustedLists unarchivedTL : unarchivedTLs) {
                    if (TLType.LOTL.equals(unarchivedTL.getType())) {
                        tlValidator.checkLOTL(unarchivedTL);
                    } else {
                        tlValidator.checkTL(unarchivedTL, potentialSigners.get(unarchivedTL.getTerritory().getCodeTerritory()));
                    }
                }
            }
        }
        LOGGER.debug("**** END SIGNATURE VALIDATION " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.CHECKSIGNATURE, AuditStatus.SUCCES, "", 0, "SYSTEM", "End signature validation job");
    }
}
