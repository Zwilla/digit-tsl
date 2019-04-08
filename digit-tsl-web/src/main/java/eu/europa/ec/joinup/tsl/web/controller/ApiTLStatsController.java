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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import eu.europa.ec.joinup.tsl.business.dto.data.stats.StatisticExtractionCriterias;
import eu.europa.ec.joinup.tsl.business.service.TLStatisticService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@SessionAttributes(value = { "csvExtract", "criteria" })
@RequestMapping(value = "/api/stats/")
public class ApiTLStatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTLStatsController.class);

    @Autowired
    private TLStatisticService tlStatisticService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/criteria", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<StatisticExtractionCriterias> getCriteria() {
        ServiceResponse<StatisticExtractionCriterias> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(tlStatisticService.initCriteria());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> perfomExtract(@RequestBody StatisticExtractionCriterias criteria, HttpSession session, Model model) {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            session.removeAttribute("csvExtract");
            session.removeAttribute("criteria");
            model.addAttribute("csvExtract", tlStatisticService.generateCSV(criteria));
            model.addAttribute("criteria", criteria);
            return new ServiceResponse<>(HttpStatus.OK.toString());
        } else {
            return new ServiceResponse<>(HttpStatus.UNAUTHORIZED.toString());
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response, HttpSession session, @ModelAttribute("csvExtract") ByteArrayOutputStream csvExtract,
            @ModelAttribute("criteria") StatisticExtractionCriterias criteria) {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if ((csvExtract == null) || (criteria == null)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                try {
                    response.reset();
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + "TL Stats - " + criteria.getDynamicName() + ".csv\"");
                    response.setContentLength(csvExtract.size());
                    response.setContentType(MediaType.TEXT_XML_VALUE);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    csvExtract.writeTo(response.getOutputStream());
                } catch (IOException e) {
                    LOGGER.error("Error while writing generated CSV to HttpReponse", e);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            session.removeAttribute("csvExtract");
            session.removeAttribute("criteria");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

}
