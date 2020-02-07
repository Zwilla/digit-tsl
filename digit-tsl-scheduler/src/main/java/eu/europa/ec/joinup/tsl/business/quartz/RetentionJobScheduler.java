package eu.europa.ec.joinup.tsl.business.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.service.RetentionJobService;

@Service
public class RetentionJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetentionJobScheduler.class);

    @Autowired
    private RetentionJobService retentionJobService;

    @Scheduled(cron = "${cron.retention.job}")
    public void start() {
        LOGGER.debug("RETENTION CRON JOBS --> RetentionJobService.start()");
        retentionJobService.start(false);
    }

}
