package eu.europa.ec.joinup.tsl.business.quartz;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.repository.TLRepository;
import eu.europa.ec.joinup.tsl.business.service.LoadingJobService;

@Service
public class LoadingJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingJobScheduler.class);

    @Value("${load.lotl.on.startup:false}")
    private boolean loadLotlOnStartup = false;

    @Autowired
    private TLRepository tlRepository;

    @Autowired
    private LoadingJobService loadingJobService;

    @PostConstruct
    public void launchIfEmpty() {
        if (loadLotlOnStartup && (tlRepository.count() == 0)) {
            loadLOTL();
        }
    }

    @Scheduled(cron = "${cron.loading.job}")
    public void loadLOTL() {
        LOGGER.debug("LOAD LOTL CRON JOBS --> loadingJobService.start()");
        loadingJobService.start();
    }

}
