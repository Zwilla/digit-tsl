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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Controller
public class ApiEnumController {

    @Autowired
    private UserService userService;

    @RequestMapping("/js/tag.js")
    public ResponseEntity<String> loadScript() throws Exception {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            StringBuffer sb = new StringBuffer();
            sb.append("function initScope($scope) {\n");
            for (Tag myTag : Tag.values()) {
                sb.append("$scope.TAG_");
                sb.append(myTag);
                sb.append(" = '");
                sb.append(myTag);
                sb.append("';\n");
            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/js/checkStatus.js")
    public ResponseEntity<String> loadCheckStatus() throws Exception {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            StringBuffer sb = new StringBuffer();
            sb.append("function initStatusEnum($scope) {\n");
            sb.append("$scope.STATUS=[];\n");
            for (CheckStatus myEnum : CheckStatus.values()) {
                if (!myEnum.equals(CheckStatus.SUCCESS)) {
                    sb.append("$scope.STATUS.push(\"");
                    sb.append(myEnum);
                    sb.append("\");\n");
                }

            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/js/checkImpact.js")
    public ResponseEntity<String> loadCheckImpact() throws Exception {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            StringBuffer sb = new StringBuffer();
            sb.append("function initImpactEnum($scope) {\n");
            sb.append("$scope.IMPACT=[];\n");
            for (CheckImpact myEnum : CheckImpact.values()) {
                sb.append("$scope.IMPACT.push(\"");
                sb.append(myEnum);
                sb.append("\");\n");
            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
