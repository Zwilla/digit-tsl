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

import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.CacheService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/cache")
public class ApiCacheController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiCacheController.class);

	@Autowired
	private CacheService cacheService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuditService auditService;

	@RequestMapping(value = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ServiceResponse<String> evictCountries() {
		ServiceResponse<String> response = new ServiceResponse<String>();
		if ((SecurityContextHolder.getContext().getAuthentication() != null)
				&& userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
			try {
				cacheService.evictCountryCache();
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN COUNTRIES");
				response.setResponseStatus(HttpStatus.OK.toString());
			} catch (Exception e) {
				LOGGER.error("Unable to evict country cache : " + e.getMessage(), e);
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.ERROR, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN COUNTRIES");
				response.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			}
		} else {
			response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
		}
		return response;
	}

	@RequestMapping(value = "/properties", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ServiceResponse<String> evictProperties() {
		ServiceResponse<String> response = new ServiceResponse<String>();
		if ((SecurityContextHolder.getContext().getAuthentication() != null)
				&& userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
			try {
				cacheService.evictPropertiesCache();
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN PROPERTIES");
				response.setResponseStatus(HttpStatus.OK.toString());
			} catch (Exception e) {
				LOGGER.error("Unable to evict properties cache : " + e.getMessage(), e);
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.ERROR, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN PROPERTIES");
				response.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			}
		} else {
			response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
		}
		return response;
	}

	@RequestMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ServiceResponse<String> evictCheck() {
		ServiceResponse<String> response = new ServiceResponse<String>();
		if ((SecurityContextHolder.getContext().getAuthentication() != null)
				&& userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
			try {
				cacheService.evictCheckCache();
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.SUCCES, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN CHECK");
				response.setResponseStatus(HttpStatus.OK.toString());
			} catch (Exception e) {
				LOGGER.error("Unable to evict check cache : " + e.getMessage(), e);
				auditService.addAuditLog(AuditTarget.CACHE, AuditAction.EXECUTE, AuditStatus.ERROR, "", 0,
						SecurityContextHolder.getContext().getAuthentication().getName(), "CLEAN CHECK");
				response.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			}
		} else {
			response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
		}
		return response;
	}

}
