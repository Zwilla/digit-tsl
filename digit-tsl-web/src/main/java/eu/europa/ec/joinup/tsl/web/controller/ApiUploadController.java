package eu.europa.ec.joinup.tsl.web.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.ec.joinup.tsl.business.dto.Load;
import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLDraftService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.TLValidator;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.esig.dss.x509.CertificateToken;

@Controller
public class ApiUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUploadController.class);

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private TLService tlService;

    @Autowired
    private TLValidator tlValidator;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/fileUpload/{cookieId}", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TrustedListsReport> importParse(@RequestParam("file") MultipartFile myFile, @PathVariable String cookieId) {
        ServiceResponse<TrustedListsReport> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (myFile != null) {
                try {
                    Load load = new Load();
                    // if isnew -> Migrated
                    load.setNew(false);
                    String userName = SecurityContextHolder.getContext().getAuthentication().getName();
                    DBTrustedLists newDraft = draftService.createDraftFromXML(myFile.getBytes(), cookieId, userName, load);

                    // CHECK SIGNATURE
                    tlValidator.validateTLSignature(newDraft);

                    // EXECUTE ALL CHECK
                    TL draft = tlService.getTL(newDraft.getId());
                    rulesRunner.runAllRulesByTL(draft);
                    tlService.setTlCheckStatus(draft.getTlId());

                    TrustedListsReport tlInfo = tlService.getTLInfo(newDraft.getId());
                    tlInfo.setMigrate(load.isNew());
                    response.setContent(tlInfo);
                    response.setResponseStatus(HttpStatus.OK.toString());

                } catch (Exception e) {
                    LOGGER.error("Cannot parse and import TL : " + e.getMessage(), e);
                    response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                }
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/certificateInfo", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLDigitalIdentification> importCertificate(@RequestParam("file") MultipartFile myFile) {
        ServiceResponse<TLDigitalIdentification> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (myFile != null) {
                try {
                    CertificateToken certToken = CertificateTokenUtils.loadCertificate(myFile.getBytes());
                    if (certToken != null) {
                        TLDigitalIdentification tlDigitalId = new TLDigitalIdentification(certToken.getEncoded());
                        if (tlDigitalId.getCertificateList() != null) {
                            response.setContent(tlDigitalId);
                            response.setResponseStatus(HttpStatus.OK.toString());
                        } else {
                            LOGGER.error("Certificate LIST NULL");
                            response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                        }
                    } else {
                        LOGGER.error("Certificate LIST NULL");
                        response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    }

                } catch (IOException e) {
                    LOGGER.error("Cannot parse and import Certificate : " + e.getMessage(), e);
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

    @RequestMapping(value = "/certificateB64", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<TLDigitalIdentification> importCertB64(@RequestBody String base64) {
        ServiceResponse<TLDigitalIdentification> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            CertificateToken certToken = CertificateTokenUtils.loadCertificate(base64);
            assert certToken != null;
            response.setContent(new TLDigitalIdentification(certToken.getEncoded()));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
