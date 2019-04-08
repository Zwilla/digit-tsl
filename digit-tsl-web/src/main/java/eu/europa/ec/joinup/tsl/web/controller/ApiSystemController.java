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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.Column;
import eu.europa.ec.joinup.tsl.business.service.BackboneColumnsService;
import eu.europa.ec.joinup.tsl.business.service.ErrorAnalysisService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/system")
public class ApiSystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSystemController.class);

    @Autowired
    private BackboneColumnsService systemService;

    @Autowired
    private ErrorAnalysisService errorAnalysisService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/columns", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<List<Column>> getTBColumn() {
        ServiceResponse<List<Column>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(systemService.getColumns());
            response.setResponseStatus(HttpStatus.OK.toString());

        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/download/checkAnalysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void download(HttpServletRequest request, HttpServletResponse response) {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                byte[] out = errorAnalysisService.getErrorAnalysis();
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", "attachment; filename=checkAnalysis.xls");
                IOUtils.write(out, response.getOutputStream());
            } catch (Exception e) {
                LOGGER.error("An error occured while generating excel check analysis", e);
            }
        }
    }

}
