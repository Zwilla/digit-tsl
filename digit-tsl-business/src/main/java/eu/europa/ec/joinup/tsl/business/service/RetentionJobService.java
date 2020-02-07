package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

/**
 * Job for retention policy clean
 */
@Service
public class RetentionJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetentionJobService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private RetentionService retentionService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    /**
     * Run retention job service
     *
     * @param overdo
     *            : true to bypass the application property verification
     */
    public void start(boolean overdo) {
        if (overdo || applicationPropertyService.runRetentionPolicy()) {
            LOGGER.debug("**** START RETENTION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.RETENTION, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start retention job");
            retentionService.retentionClean();
            LOGGER.debug("**** END RETENTION JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.RETENTION, AuditStatus.SUCCES, "", 0, "SYSTEM", "End retention job");
        }
    }
}
