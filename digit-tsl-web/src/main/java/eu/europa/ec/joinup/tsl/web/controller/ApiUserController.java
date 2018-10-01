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

import java.util.ResourceBundle;

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

import eu.europa.ec.joinup.tsl.business.dto.User;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLUser;
import eu.europa.ec.joinup.tsl.web.form.UserForm;

@Controller
@RequestMapping(value = "/api/user")
public class ApiUserController {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<User> getConnectedUserInfo() {
        ServiceResponse<User> response = new ServiceResponse<>();
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user == null) {
                response.setContent(new User());
                response.setErrorMessage(bundle.getString("appController.errorUserLoading"));
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            } else {
                response.setContent(user);
                response.setResponseStatus(HttpStatus.OK.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.SERVICE_UNAVAILABLE.toString());
        }
        return response;
    }

    @RequestMapping(value = "/addRole/", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> addRole(@RequestBody TLUser userDto) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (!userService.getCodeRole(userDto.getRoleId()).equalsIgnoreCase("SUP")) {
                if (userService.addRole(userDto.getUserId(), userDto.getRoleId())) {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.SUCCES, "", 0,
                            SecurityContextHolder.getContext().getAuthentication().getName(), "ADD ROLE;USER ECAS ID:" + userDto.getUserId() + ";ROLEID:" + userDto.getRoleId());
                    response.setResponseStatus(HttpStatus.OK.toString());
                } else {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.ERROR, "", 0,
                            SecurityContextHolder.getContext().getAuthentication().getName(), "ADD ROLE;USER ECAS ID:" + userDto.getUserId() + ";ROLEID:" + userDto.getRoleId());
                    response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                }
            } else {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    @RequestMapping(value = "/removeRole/", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> removeRole(@RequestBody TLUser userDto) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            String username = userService.getDbUserName(userDto.getUserId());
            if (userService.removeRole(userDto.getUserId(), userDto.getRoleId())) {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.SUCCES, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(), "REMOVE ROLE; USER ECAS ID:" + username + ";ROLEID:" + userDto.getRoleId());
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.ERROR, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(), "REMOVE ROLE;USER ECAS ID:" + username + ";ROLEID:" + userDto.getRoleId());
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/addUser/", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<User> addUser(@RequestBody UserForm userForm) {
        ServiceResponse<User> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (StringUtils.isEmpty(userForm.getEcasId())) {
                response.setErrorMessage(bundle.getString("user.id.null"));
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            } else if (userService.userExist(userForm.getEcasId())) {
                response.setErrorMessage(bundle.getString("user.already.exist").replace("%ID%", userForm.getEcasId()));
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            } else {
                User user = userService.addUser(userForm.getEcasId(), userForm.getCountry());
                if (user == null) {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.CREATE, AuditStatus.ERROR, "", 0,
                            SecurityContextHolder.getContext().getAuthentication().getName(), "NEW USER");
                    response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                } else {
                    auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.CREATE, AuditStatus.SUCCES, "", 0,
                            SecurityContextHolder.getContext().getAuthentication().getName(), "NEW USER:" + user.getEcasId());
                    response.setContent(user);
                    response.setResponseStatus(HttpStatus.OK.toString());
                }
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/updateTerritory", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> updateTerritory(@RequestBody UserForm userForm) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (userService.updateTerritory(userForm.getEcasId(), userForm.getCountry())) {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.SUCCES, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        "UPDATE TERRITORY;USER ECAS ID:" + userForm.getEcasId() + ";COUNTRY:" + userForm.getCountry());
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.UPDATE, AuditStatus.ERROR, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(), "UPDATE TERRITORY");
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/delUser/", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<User> removeUser(@RequestBody int userId) {
        ServiceResponse<User> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            String username = userService.getDbUserName(userId);
            if (userService.deleteUser(userId)) {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.DELETE, AuditStatus.SUCCES, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(), "DELETE USER ECAS ID : " + username);
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_USER, AuditAction.DELETE, AuditStatus.ERROR, "", 0,
                        SecurityContextHolder.getContext().getAuthentication().getName(), "DELETE USER ECAS ID : " + username);
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
