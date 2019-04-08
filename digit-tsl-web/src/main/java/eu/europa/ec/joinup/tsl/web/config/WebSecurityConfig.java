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
package eu.europa.ec.joinup.tsl.web.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.header.writers.XContentTypeOptionsHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;

import eu.europa.ec.joinup.tsl.business.repository.AuditRepository;
import eu.europa.ec.joinup.tsl.business.repository.UserRepository;
import eu.europa.ec.joinup.tsl.model.DBAudit;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Value("${casServerUrl}")
    private String casServerUrl;

    @Value("${casServiceUrl}")
    private String casServiceUrl;

    @Value("${casProxyPropertie}")
    private String casProxyPropertie;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditRepository auditRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/notification**", "/notification/**").authenticated().antMatchers("/modalAvailabilityChart").authenticated().antMatchers("/tl**", "/tl/**")
                .authenticated().antMatchers("/").authenticated().antMatchers("/home").authenticated().antMatchers("/management**", "/management/**").authenticated()
                .antMatchers("/drafts**", "/drafts/**").authenticated().antMatchers("/news").authenticated().antMatchers("/wizard**", "/wizard/**").authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).maximumSessions(1);

        http.addFilter(authenticationFilter());
        http.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
        http.csrf().csrfTokenRepository(csrfTokenRepository());
        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());

        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN)).addHeaderWriter(new XContentTypeOptionsHeaderWriter())
                .addHeaderWriter(new XXssProtectionHeaderWriter()).addHeaderWriter(new CacheControlHeadersWriter()).addHeaderWriter(new HstsHeaderWriter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(authenticationProvider());
    }

    public CasAuthenticationProvider authenticationProvider() {

        CasAuthenticationProvider provider = new CasAuthenticationProvider();

        Cas20ProxyTicketValidatorCustom ticketValidator = new Cas20ProxyTicketValidatorCustom(casServerUrl, casProxyPropertie);
        provider.setTicketValidator(ticketValidator);

        provider.setAuthenticationUserDetailsService(service2);
        provider.setKey("digit-tsl");
        provider.setServiceProperties(serviceProperties());

        return provider;
    }

    public CasAuthenticationFilter authenticationFilter() throws Exception {

        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;

    }

    AuthenticationUserDetailsService<CasAssertionAuthenticationToken> service2 = new AuthenticationUserDetailsService<CasAssertionAuthenticationToken>() {
        @Override
        public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
            if (token.getName() == null) {
                throw new UsernameNotFoundException(bundle.getString("user.id.null"));
            }

            DBAudit dbAudit = new DBAudit();
            dbAudit.setTarget(AuditTarget.USER);
            dbAudit.setAction(AuditAction.LOGIN);
            dbAudit.setDate(new Date());
            dbAudit.setUsername(token.getName());
            dbAudit.setFileId(0);

            DBUser dbUser = userRepository.findByEcasIdAndRoleNotNull(token.getName());
            if (dbUser == null) {
                dbAudit.setStatus(AuditStatus.ERROR);
                dbAudit.setCountryCode("EU");
                dbAudit.setInfos("Authentication error");
                auditRepository.save(dbAudit);

                throw new UsernameNotFoundException(bundle.getString("user.id.not.authorized").replace("%ID%", token.getName()));
            } else {
                dbAudit.setStatus(AuditStatus.SUCCES);
                dbAudit.setCountryCode(dbUser.getTerritory().getCodeTerritory());
                dbAudit.setInfos("Authentication success");
                auditRepository.save(dbAudit);

                return new User(token.getName(), "NOT_USED", new ArrayList<GrantedAuthority>());
            }
        }
    };

    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casServiceUrl + "/j_spring_cas_security_check");
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casServerUrl + "/login");
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
