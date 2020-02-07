package eu.europa.ec.joinup.tsl.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import eu.europa.ec.joinup.tsl.business.dto.tl.TLCertificate;
import eu.europa.ec.joinup.tsl.business.dto.tl.TLDigitalIdentification;
import eu.europa.ec.joinup.tsl.business.service.AbstractAlertingService;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;

@Controller
@RequestMapping(value = "/api/keyStore")
public class ApiKeyStoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyStoreController.class);

    @Autowired
    KeyStoreCertificateSource keyStoreCertificateSource;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private AbstractAlertingService alertingService;

    @Value("${lotl.keystore.file}")
    private String lotlKeyStoreFilename;

    @RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLCertificate>> getCertificatesDTOFromKeyStore() {
        ServiceResponse<List<TLCertificate>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            List<TLCertificate> list = new ArrayList<>();
            List<CertificateToken> certificatesFromKeyStore = CertificateTokenUtils.sortCertificateList(keyStoreCertificateSource.getCertificates());
            for (CertificateToken certificateToken : certificatesFromKeyStore) {
                TLCertificate cert = new TLCertificate(certificateToken.getEncoded());
                cert.setId(certificateToken.getDSSIdAsString());
                list.add(cert);
            }

            response.setContent(list);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> addB64(@RequestBody String base64) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try (FileOutputStream fos = new FileOutputStream(new File(lotlKeyStoreFilename))) {
                CertificateToken certificateToken = CertificateTokenUtils.loadCertificate(base64);
                assert certificateToken != null;
                assert certificateToken != null;
                keyStoreCertificateSource.addCertificateToKeyStore(certificateToken);
                keyStoreCertificateSource.store(fos);
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_LIST_SIGNING_CERT, AuditAction.CREATE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "CERTTOKEN:" + certificateToken.getDSSIdAsString());
                response.setResponseStatus(HttpStatus.OK.toString());
                alertingService.sendNewSigningCertificate();
            } catch (DSSException | IOException e) {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                LOGGER.error("Add new certificate in KeyStore ", e);
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<String> deleteCertificateFromKeyStore(@RequestBody String id) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try (FileOutputStream fos = new FileOutputStream(new File(lotlKeyStoreFilename))) {
                keyStoreCertificateSource.deleteCertificateFromKeyStore(id);
                keyStoreCertificateSource.store(fos);
                auditService.addAuditLog(AuditTarget.ADMINISTRATION_LIST_SIGNING_CERT, AuditAction.DELETE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                        "CERTID:" + id);
                response.setResponseStatus(HttpStatus.OK.toString());
                alertingService.sendNewSigningCertificate();
            } catch (DSSException | IOException e) {
                LOGGER.error("Delete certificate from keystore error", e);
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/certPointer", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<TLDigitalIdentification>> getDigitalIdentityList() {
        ServiceResponse<List<TLDigitalIdentification>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            List<TLDigitalIdentification> digitalList = new ArrayList<>();

            List<CertificateToken> certificatesFromKeyStore = CertificateTokenUtils.sortCertificateList(keyStoreCertificateSource.getCertificates());
            for (CertificateToken certificateToken : certificatesFromKeyStore) {
                TLDigitalIdentification digitalId = new TLDigitalIdentification();
                TLCertificate cert = new TLCertificate(certificateToken.getEncoded());
                List<TLCertificate> certList = new ArrayList<>();
                certList.add(cert);
                digitalId.setCertificateList(certList);
                digitalList.add(digitalId);
            }

            response.setContent(digitalList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
