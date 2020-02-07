package eu.europa.ec.joinup.tsl.business.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;
import eu.europa.ec.joinup.tsl.business.service.RetentionJobService;
import eu.europa.ec.joinup.tsl.business.service.RulesValidationJobService;
import eu.europa.ec.joinup.tsl.business.service.SignatureAlertingJobService;
import eu.europa.ec.joinup.tsl.business.service.SignatureValidationJobService;
import eu.europa.ec.joinup.tsl.business.service.TLBreakAlertingJobService;

/**
 * Run different jobs in asynchronous way
 */
@Service
public class ASyncJobs {

    @Autowired
    private LoadingJobService loadingJobService;

    @Autowired
    private RulesValidationJobService rulesValidationJobService;

    @Autowired
    private SignatureValidationJobService signatureValidationJobService;

    @Autowired
    private RetentionJobService retentionJobService;

    @Autowired
    private SignatureAlertingJobService signatureAlertJobService;

    @Autowired
    private TLBreakAlertingJobService tlBreakAlertJobService;

    @Async
    public void launchLoading() {
        loadingJobService.start();
    }

    @Async
    public void launchRulesValidation() {
        rulesValidationJobService.start();
    }

    @Async
    public void launchSignatureValidation() {
        signatureValidationJobService.start();
    }

    @Async
    public void launchRetentionPolicy() {
        retentionJobService.start(true);
    }

    @Async
    public void launchSignatureAlert() {
        signatureAlertJobService.start();
    }

    @Async
    public void launchApproachBreakAlert() {
        tlBreakAlertJobService.start();
    }
}
