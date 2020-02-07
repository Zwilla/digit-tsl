package eu.europa.ec.joinup.tsl.business;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.europa.ec.joinup.tsl.business.service.AlertingService;

@Configuration
public class AlertingTestConfig {

    @Bean
    public AlertingService initAlertingService() {
        return new AlertingService();
    }

}
