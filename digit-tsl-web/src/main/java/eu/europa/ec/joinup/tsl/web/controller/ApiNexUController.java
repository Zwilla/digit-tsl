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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TLValidator;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TlSignatureNexUInformation;
import eu.europa.ec.joinup.tsl.web.util.SignatureUtil;
import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.MimeType;
import eu.europa.esig.dss.SignatureAlgorithm;
import eu.europa.esig.dss.SignatureValue;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import lu.nowina.nexu.object.model.SignatureRequest;

@Controller
@SessionAttributes(value = "params")
@RequestMapping(value = "/api/signature/nexU")
public class ApiNexUController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSignatureController.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private RulesRunnerService rulesRunnerService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private AuditService auditService;

    @Value("${nexu.msi.file}")
    private String nexuMsiFile;

    @RequestMapping(value = "/getTbs", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<SignatureRequest> getToBeSigned(@RequestBody TlSignatureNexUInformation signatureObject, Model model) {
        ServiceResponse<SignatureRequest> response = new ServiceResponse<>();
        try {
            CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
            XAdESService signatureService = new XAdESService(certificateVerifier);
            // remove signature
            tlService.createFileWithoutSignature(signatureObject.getTlId());
            InputStream is = tlService.getXmlStreamToSign(signatureObject.getTlId());
            DSSDocument doc = new InMemoryDocument(IOUtils.toByteArray(is));
            doc.setMimeType(MimeType.XML);
            XAdESSignatureParameters params = SignatureUtil.getSignatureParams(signatureObject.getCertificateResponse(), doc);

            model.addAttribute("params", params);

            SignatureRequest tbs = SignatureUtil.getToBeSigned(signatureObject.getCertificateResponse(), signatureService, doc, params);

            response.setContent(tbs);
            response.setResponseStatus(HttpStatus.OK.toString());
        } catch (DSSException e) {
            LOGGER.error("Error during signature : " + e.getMessage(), e);
            response.setErrorMessage(e.getMessage());
            response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
        } catch (Exception e) {
            LOGGER.error("Error during signature : " + e.getMessage(), e);
            response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
        }
        return response;
    }

    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLSignature> sign(@RequestBody TlSignatureNexUInformation signatureObject, @ModelAttribute("params") XAdESSignatureParameters params) {
        ServiceResponse<TLSignature> response = new ServiceResponse<>();
        try {
            CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
            XAdESService signatureService = new XAdESService(certificateVerifier);

            InputStream is = tlService.getXmlStreamToSign(signatureObject.getTlId());
            DSSDocument doc;

            doc = new InMemoryDocument(IOUtils.toByteArray(is));

            SignatureValue signatureValue = new SignatureValue(SignatureAlgorithm.RSA_SHA256, signatureObject.getSignatureValue());
            DSSDocument signedDoc = signatureService.signDocument(doc, params, signatureValue);

            TL tl = tlService.getTL(signatureObject.getTlId());
            auditService.addAuditLog(AuditTarget.DRAFT_TL, AuditAction.SIGN, AuditStatus.SUCCES, tl.getSchemeInformation().getTerritory(), 0,
                    SecurityContextHolder.getContext().getAuthentication().getName(), "TLID:" + signatureObject.getTlId());
            tlService.updateSignedXMLFile(signedDoc, signatureObject.getTlId());

            //Check all TL
            rulesRunnerService.validDraftAfterSignature(signatureObject.getTlId());
            //Check signature
            tlValidator.checkAllSignature(tlService.getDbTL(signatureObject.getTlId()));
            response.setResponseStatus(HttpStatus.OK.toString());
            TLSignature signature = tlService.getSignatureInfo(signatureObject.getTlId());
            response.setContent(signature);
        } catch (Exception e) {
            LOGGER.error("Error during signature : " + e.getMessage(), e);
            response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
        }
        return response;

    }

    @RequestMapping(value = "/file_url", method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<String> getNexuZipUrl() {
        ServiceResponse<String> response = new ServiceResponse<>();
        response.setResponseStatus(HttpStatus.OK.toString());
        response.setContent(nexuMsiFile);
        return response;
    }
}
