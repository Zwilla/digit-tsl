package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:application-tlmanager-custom.properties", ignoreResourceNotFound = true)
public class OverrideConfig {

    @Import(ApplicationConfig.class)
    static class InnerConfig {
    }

}
