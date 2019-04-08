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
package eu.europa.ec.joinup.tsl.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.quartz.ASyncJobs;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.LOTLValidationService;
import eu.europa.ec.joinup.tsl.business.service.TLDataLoaderService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/jobs")
public class ApiJobsController {

    private static final Logger logger = LoggerFactory.getLogger(ApiJobsController.class);

    @Autowired
    private ASyncJobs asyncJobs;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private LOTLValidationService lotlValidationService;

    @Autowired
    private TLDataLoaderService tlDataLoaderService;

    @RequestMapping(value = "/loading", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> loading() {
        logger.info("Loading job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(), "RUN LOADING");
            asyncJobs.launchLoading();
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/rulesValidation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> rulesValidation() {
        logger.info("Rules Validation job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(), "RUN RULES VALIDATION");
            asyncJobs.launchRulesValidation();
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/signatureValidation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> signatureValidation() {
        logger.info("Signature validation job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(), "RUN SIGNATURE VALIDATION");
            asyncJobs.launchSignatureValidation();
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/retentionPolicy", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> retentionValidation() {
        logger.info("Retention validation job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(), "RUN RETENTION POLICY");
            asyncJobs.launchRetentionPolicy();
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/serviceDataRefresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<String> serviceDateRefresh() {
        logger.info("Service data refresh job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            auditService.addAuditLog(AuditTarget.JOBS, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                    "RUN SERVICE DATA REFRESH - START");
            tlDataLoaderService.refreshAllTablesValue();
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/validate_lotl", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<String> validateLOTL() {
        logger.info("LOTL validation job");
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            lotlValidationService.lotlValidation(SecurityContextHolder.getContext().getAuthentication().getName());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
