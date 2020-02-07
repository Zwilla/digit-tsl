package eu.europa.ec.joinup.tsl.business.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.SignatureValidationJobService;

@Service
public class SignaturesValidationJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignaturesValidationJobScheduler.class);

    @Autowired
    SignatureValidationJobService signatureValidationJobService;

    @Scheduled(cron = "${cron.signature.validation.job}")
    public void check() {
        LOGGER.debug("check CRON JOBS --> signatureValidationJobService.start()");
        signatureValidationJobService.start();
    }

}
