package eu.europa.ec.joinup.tsl.web;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import eu.europa.ec.joinup.tsl.web.config.ApplicationConfig;
import eu.europa.ec.joinup.tsl.web.config.DataLoaderConfig;
import eu.europa.ec.joinup.tsl.web.config.OverrideConfig;
import eu.europa.ec.joinup.tsl.web.config.PersistenceConfig;
import eu.europa.ec.joinup.tsl.web.config.ProxyConfiguration;
import eu.europa.ec.joinup.tsl.web.config.WebConfig;
import eu.europa.ec.joinup.tsl.web.config.WebSecurityConfig;

@Order(2)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        final String username = System.getProperty("http.proxyUsername");
        final String password = System.getProperty("http.proxyPassword");

        if ((username != null) && (password != null)) {
            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            };
            Authenticator.setDefault(auth);
        }
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    protected Class<?>[] getRootConfigClasses() {
        Class config = null;
        try {
            config = Class.forName("eu.europa.ec.joinup.tsl.business.config.FreemarkerConfig");
        } catch (ClassNotFoundException e) {
            // Additional freemarker config not found
        }
        List<Class> configs =
                new ArrayList<Class>(Arrays.asList(ApplicationConfig.class, OverrideConfig.class, PersistenceConfig.class, DataLoaderConfig.class, WebSecurityConfig.class, ProxyConfiguration.class));
        if (config != null) {
            configs.add(config);
        }
        return configs.toArray(new Class[0]);
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter };
    }

}
