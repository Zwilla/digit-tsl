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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBUser;

@Controller
@RequestMapping(value = "/management")
public class ManagementController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userManagement", method = RequestMethod.GET)
    public String tl(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "userManagement";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/dataProperties"
    }, method = RequestMethod.GET)
    public String dataProperties(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataProperties";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/dataChecks"
    }, method = RequestMethod.GET)
    public String dataChecks(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataChecks";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/signingCertificate"
    }, method = RequestMethod.GET)
    public String keyStore(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "signingCertificate";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/system"
    }, method = RequestMethod.GET)
    public String system(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "system";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/audit"
    }, method = RequestMethod.GET)
    public String audit(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "audit";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/dataRetention"
    }, method = RequestMethod.GET)
    public String dataRetention(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataRetention";
            }
        }
        return "denied";
    }

    @RequestMapping(value = {
            "/logs"
    }, method = RequestMethod.GET)
    public String logs(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "application_logs";
            }
        }
        return "denied";
    }

}
