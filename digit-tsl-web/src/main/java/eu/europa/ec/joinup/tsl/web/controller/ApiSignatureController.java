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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TLValidator;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBUser;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.signature.SignatureService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLSignatureInformation;
import eu.europa.esig.dss.InMemoryDocument;

@Controller
@RequestMapping(value = "/api/signature")
public class ApiSignatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSignatureController.class);

    @Autowired(required = false)
    private SignatureService signature;

    @Autowired
    private TLService tlService;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @Value("${ms:true}")
    private boolean ms;

    @RequestMapping(value = "/sealList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<List<String>> getSeal() {
        ServiceResponse<List<String>> response = new ServiceResponse<>();
        if (ms) {
            response.setContent(new ArrayList<String>());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
                if ((userConnected != null) && userService.islotlSealer(userConnected.getEcasId())) {
                    List<String> sealNames = new ArrayList<>();
                    for (String seal : signature.getSealDisplayNames()) {
                        if (!StringUtils.isEmpty(seal)) {
                            sealNames.add(seal);
                        }
                    }
                    response.setContent(sealNames);
                    response.setResponseStatus(HttpStatus.OK.toString());
                } else {
                    response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
                }
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        }
        return response;
    }

    @RequestMapping(value = "/signTl", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLSignature> signTL(@RequestBody TLSignatureInformation tlSignatureInformation) {
        ServiceResponse<TLSignature> response = new ServiceResponse<>();
        // remove signature
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.islotlSealer(SecurityContextHolder.getContext().getAuthentication().getName())) {
            tlService.createFileWithoutSignature(tlSignatureInformation.getTlId());
            InputStream is = tlService.getXmlStreamToSign(tlSignatureInformation.getTlId());
            byte[] signFile;
            try {
                signFile = signature.sign(IOUtils.toByteArray(is), tlSignatureInformation.getSealName());
                auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.SEAL, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "SEAL:" + tlSignatureInformation.getSealName() + ";TLID:" + tlSignatureInformation.getTlId());
                tlService.updateSignedXMLFile(new InMemoryDocument(signFile), tlSignatureInformation.getTlId());
                //Check all TL
                rulesRunner.validDraftAfterSignature(tlSignatureInformation.getTlId());
                //Check signature
                tlValidator.checkAllSignature(tlService.getDbTL(tlSignatureInformation.getTlId()));
                response.setResponseStatus(HttpStatus.OK.toString());
                response.setContent(tlService.getSignatureInfo(tlSignatureInformation.getTlId()));
            } catch (Exception e) {
                LOGGER.error("Unable to sign the TL : " + e.getMessage(), e);
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

}
