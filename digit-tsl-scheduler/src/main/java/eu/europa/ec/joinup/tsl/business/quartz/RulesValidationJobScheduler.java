package eu.europa.ec.joinup.tsl.business.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.RulesValidationJobService;

@Service
public class RulesValidationJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RulesValidationJobScheduler.class);

    @Autowired
    RulesValidationJobService rulesValidationJobService;

    @Scheduled(cron = "${cron.rules.validation.job}")
    public void runRules() {
        LOGGER.debug("runRules CRON JOBS --> runValidationJobService.start()");
        rulesValidationJobService.start();
    }

}
