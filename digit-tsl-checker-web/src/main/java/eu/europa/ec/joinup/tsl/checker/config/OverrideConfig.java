package eu.europa.ec.joinup.tsl.checker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-tlmanager-custom.properties")
public class OverrideConfig {

    @Import(ApplicationConfig.class)
    static class InnerConfig {
    }

}
