package eu.europa.ec.joinup.tsl.web;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import com.mysql.jdbc.Driver;

@Order(1)
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityWebApplicationInitializer.class);

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        super.beforeSpringSecurityFilterChain(servletContext);

        try {
            Driver driver = new Driver();
            LOGGER.info("MySQL Driver Version " + driver.getMajorVersion() + "-" + driver.getMinorVersion());
        } catch (Exception e) {
            LOGGER.warn("Cannot load driver");
        }

    }

}
