/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.web;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

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
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
                ApplicationConfig.class, OverrideConfig.class, PersistenceConfig.class, DataLoaderConfig.class, WebSecurityConfig.class, ProxyConfiguration.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                WebConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] {
                characterEncodingFilter
        };
    }

}
