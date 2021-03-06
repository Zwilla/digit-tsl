package eu.europa.ec.joinup.tsl.business.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.BreakType;
import eu.europa.ec.joinup.tsl.business.dto.tl.approachbreak.TLBreakStatus;
import eu.europa.ec.joinup.tsl.business.util.CronUtils;
import eu.europa.ec.joinup.tsl.business.util.DateUtils;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

/**
 * Alerting job for trusted list break approach
 */
@Service
@Transactional
public class TLBreakAlertingJobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLBreakAlertingJobService.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private TLService tlService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Autowired
    private TLBreakValidationService breakValidationService;

    /**
     * Trigger signature alert job
     */
    public void start() {
        LOGGER.debug("**** START TL BREAK ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.TL_BREAK_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "Start tl break alerting job");
        Date checkDate = new Date();
        for (DBCountries country : countryService.getAll()) {
            DBTrustedLists dbTL = tlService.getPublishedDbTLByCountry(country);
            if (dbTL == null) {
                LOGGER.error("BREAK ALERT - Trusted list null for country " + country.getCodeTerritory());
            } else {
                tlStatusBreakAlert(dbTL, checkDate);
            }
        }
        LOGGER.debug("**** END TL BREAK ALERTING JOB " + DateUtils.getToFormatYMDH(new Date()) + "****");
        auditService.addAuditLog(AuditTarget.JOBS, AuditAction.TL_BREAK_ALERTING, AuditStatus.SUCCES, "", 0, "SYSTEM", "End tl break alerting job");

    }

    /**
     * Send alert mail if trusted list status is break. Return 'true' if mail send
     *
     * @param dbTL
     * @param checkDate
     */
    public boolean tlStatusBreakAlert(DBTrustedLists dbTL, Date checkDate) {
        TLBreakStatus breakStatus = breakValidationService.initTLBreakStatus(dbTL, checkDate);
        if (breakStatus.getBreakType().equals(BreakType.DAY_OF_BREAK) || (isExecutionTime(checkDate) && !breakStatus.getBreakType().equals(BreakType.NONE))) {
            alertingService.sendBreakAlert(breakStatus);
            return true;
        }
        return false;
    }

    /**
     * Get CRON expression stored in database and get next execution time
     *
     * @param checkDate
     */
    private boolean isExecutionTime(final Date checkDate) {
        final Date checkDateb = DateUtils.getStartOfDay(checkDate);
        try {
            String cronExpression = applicationPropertyService.getApproachOfBreakCron();
            Date nextExecution = CronUtils.getDateFromExpression(checkDateb, cronExpression);

            return DateUtils.getStartOfDay(nextExecution).equals(checkDateb);
        } catch (Exception e) {
            LOGGER.error("BREAK ALERT - Cron expression is null or wrong", e);
            return false;
        }
    }

}
