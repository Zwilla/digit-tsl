package eu.europa.ec.joinup.tsl.web.controller;

import java.io.IOException;
import java.util.ResourceBundle;

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

import eu.europa.ec.joinup.tsl.business.dto.TLSigningCertificateResultDTO;
import eu.europa.ec.joinup.tsl.business.service.CertificateService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.business.util.CertificateTokenUtils;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.x509.CertificateToken;

@Controller
@RequestMapping("/api/wizard/signing_certificate")
public class ApiTLSigningController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserService userService;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTLSigningController.class);

    @RequestMapping(value = "/file/{territory}", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLSigningCertificateResultDTO> checkFromFile(@RequestParam("file") MultipartFile myFile, @PathVariable String territory) {
        ServiceResponse<TLSigningCertificateResultDTO> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                CertificateToken certificateToken = CertificateTokenUtils.loadCertificate(myFile.getBytes());
                if (certificateToken != null) {

                    response.setContent(certificateService.checkTLSigningCertificate(certificateToken, territory));
                    response.setResponseStatus(HttpStatus.OK.toString());
                } else {
                    response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                    response.setErrorMessage(bundle.getString("error.multipartfile.to.certificate"));
                }
            } catch (DSSException | IOException e) {
                LOGGER.error(bundle.getString("error.multipartfile.to.certificate"), e);
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                response.setErrorMessage(bundle.getString("error.multipartfile.to.certificate"));
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/b64/{territory}", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLSigningCertificateResultDTO> checkFromFile(@PathVariable String territory, @RequestBody String b64) {
        ServiceResponse<TLSigningCertificateResultDTO> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                CertificateToken certificateToken = CertificateTokenUtils.loadCertificate(b64);
                response.setContent(certificateService.checkTLSigningCertificate(certificateToken, territory));
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (DSSException e) {
                LOGGER.error(bundle.getString("error.b64.to.certificate"), e);
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                response.setErrorMessage(bundle.getString("error.b64.to.certificate"));
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }
}
