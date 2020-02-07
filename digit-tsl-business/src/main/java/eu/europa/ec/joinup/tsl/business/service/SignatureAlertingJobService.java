package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.SignatureStatus;

/**
 * Alerting job for signature status
 */
@Service
public class SignatureAlertingJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureAlertingJobService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AbstractAlertingService alertingService;

    /**
     * Trigger signature alert job
     */
    public void start() {
        LOGGER.debug("**** START SIGNATURE ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.SIGNATURE_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start signature alerting job");
        for (DBCountries country : countryService.getAll()) {
            signatureAlert(country);
        }
        LOGGER.debug("**** END SIGNATURE ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.SIGNATURE_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "End signature alerting job");
    }

    /**
     * Get DB trusted list by country & send alert mail if signature is invalid
     *
     * @param country
     */
    public void signatureAlert(DBCountries country) {
        DBTrustedLists dbTL = tlService.getPublishedDbTLByCountry(country);
        if (dbTL == null) {
            LOGGER.error("SIGNATURE ALERT - Trusted list null for country : " + country.getCodeTerritory());
        } else {
            TLSignature tmpSignature = tlService.getSignatureInfo(dbTL.getId());
            if ((tmpSignature == null) || (tmpSignature.getIndication() == null)) {
                LOGGER.error("SIGNATURE ALERT - Signature null for trusted list " + dbTL.getId());
            } else if (!tmpSignature.getIndication().equals(SignatureStatus.VALID)) {
                alertingService.sendSignatureAlert(dbTL, tmpSignature);
            }
        }
    }
}
