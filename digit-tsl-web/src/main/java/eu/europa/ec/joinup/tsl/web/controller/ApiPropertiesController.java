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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.Properties;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.CacheService;
import eu.europa.ec.joinup.tsl.business.service.CountryService;
import eu.europa.ec.joinup.tsl.business.service.PropertiesService;
import eu.europa.ec.joinup.tsl.model.DBCountries;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/properties")
public class ApiPropertiesController {

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<List<Properties>> getProperties() {
        ServiceResponse<List<Properties>> response = new ServiceResponse<List<Properties>>();
        List<Properties> propList = new ArrayList<Properties>();

        propList.addAll(propertiesService.getProperties());
        propList.addAll(countryService.getPropertiesCountry());

        response.setContent(propList);
        response.setResponseStatus(HttpStatus.OK.toString());
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<Properties> delProp(@RequestBody Properties editionProp) {
        ServiceResponse<Properties> response = new ServiceResponse<Properties>();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if ((editionProp == null) || (editionProp.getCodeList() == null)) {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_PROPERTIES, AuditAction.DELETE, AuditStatus.ERROR, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "CODELIST NULL");
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {

                if (editionProp.getCodeList().equalsIgnoreCase("COUNTRYCODENAME")) {
                    countryService.delete(editionProp.getLabel());
                } else {
                    propertiesService.delete(editionProp);
                }
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_PROPERTIES, AuditAction.DELETE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "CODELIST:" + editionProp.getCodeList());
                response.setResponseStatus(HttpStatus.OK.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/add", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<Properties> addProp(@RequestBody Properties editionProp) {
        ServiceResponse<Properties> response = new ServiceResponse<Properties>();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Properties updatedProp = null;
            if (!StringUtils.isEmpty(editionProp.getCodeList()) && !StringUtils.isEmpty(editionProp.getLabel())) {
                if (editionProp.getCodeList().equalsIgnoreCase("COUNTRYCODENAME")) {
                    DBCountries country = new DBCountries();
                    country.setCodeTerritory(editionProp.getLabel());
                    country.setCountryName(editionProp.getDescription());
                    updatedProp = countryService.add(country);
                    cacheService.evictCountryCache();
                } else {
                    updatedProp = propertiesService.add(editionProp);
                    cacheService.evictPropertiesCache();
                }
                if (updatedProp == null) {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_PROPERTIES, AuditAction.CREATE, AuditStatus.ERROR, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                            "CODELIST:" + editionProp.getCodeList());
                    response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                } else {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_PROPERTIES, AuditAction.CREATE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                            "CODELIST:" + editionProp.getCodeList());

                    response.setResponseStatus(HttpStatus.OK.toString());
                    response.setContent(updatedProp);
                }
            } else {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_PROPERTIES, AuditAction.CREATE, AuditStatus.ERROR, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "CODELIST EMPTY");
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
