package eu.europa.ec.joinup.tsl.checker.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import eu.europa.ec.joinup.tsl.checker.controller.RunTLCC;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.ws.rs.ext.RuntimeDelegate;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml", "classpath:META-INF/cxf/cxf-extension-xml.xml", "classpath:META-INF/cxf/cxf-servlet.xml" })
public class CXFConfig {

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    public JaxRsApiApplication jaxRsApiApplication() {
        return new JaxRsApiApplication();
    }

    @Bean
    public RunTLCC service() {
        return new RunTLCC();
    }

    @Bean
    public Server jaxRsServer(JacksonJsonProvider provider) {

        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(jaxRsApiApplication(), JAXRSServerFactoryBean.class);
        factory.setServiceBeans(Collections.<Object>singletonList(service()));
        factory.setProviders(Collections.<Object>singletonList(provider));

        return factory.create();
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
