package eu.europa.ec.joinup.tsl.web.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.CacheService;
import eu.europa.ec.joinup.tsl.business.service.CheckService;
import eu.europa.ec.joinup.tsl.business.service.PDFReportService;
import eu.europa.ec.joinup.tsl.business.service.RulesRunnerService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBCheck;
import eu.europa.ec.joinup.tsl.model.enums.AuditAction;
import eu.europa.ec.joinup.tsl.model.enums.AuditStatus;
import eu.europa.ec.joinup.tsl.model.enums.AuditTarget;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
import eu.europa.ec.joinup.tsl.web.form.TLCookie;

@Controller
@RequestMapping(value = "/api/checks")
public class ApiChecksController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiChecksController.class);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private TLService tlService;

    @Autowired
    private CheckService checkService;

    @Autowired
    private PDFReportService reportService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private RulesRunnerService rulesRunner;

    @Autowired
    private UserService userService;

    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<CheckDTO>> getChecks() {
        ServiceResponse<List<CheckDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(checkService.getAll());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/errors", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<List<CheckDTO>> getErrorChecks(@RequestBody TLCookie cookie) {
        ServiceResponse<List<CheckDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
                response.setContent(checkService.getTLChecks(cookie.getTlId()));
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public @ResponseBody ServiceResponse<CheckDTO> editCheck(@RequestBody CheckDTO editionCheck) {
        ServiceResponse<CheckDTO> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            DBCheck updatedCheck = checkService.edit(editionCheck);
            if (updatedCheck.getPriority().equals(CheckStatus.IGNORE)) {
                checkService.deleteCheckResult(updatedCheck);
            }
            auditService.addAuditLog(AuditTarget.ADMINISTRATION_CHECKS, AuditAction.UPDATE, AuditStatus.SUCCES, "", 0, SecurityContextHolder.getContext().getAuthentication().getName(),
                    "UPDATE CHECK:" + updatedCheck.getId() + "IMPACT:" + updatedCheck.getImpact());
            cacheService.evictCheckCache();
            response.setResponseStatus(HttpStatus.OK.toString());
            response.setContent(new CheckDTO(updatedCheck));
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/download/{tlId}/{draftStoreId}", method = RequestMethod.GET)
    public void downloadCheckReport(HttpSession session, HttpServletResponse response, @PathVariable int tlId, @PathVariable String draftStoreId) {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStoreOrProd(tlId, draftStoreId)) {
                try {
                    TL tl = tlService.getTL(tlId);
                    if (tl != null) {
                        response.setContentType(eu.europa.esig.dss.MimeType.PDF.getMimeTypeString());
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + tl.getSchemeInformation().getTerritory() + "(Sn" + tl.getSchemeInformation().getSequenceNumber() + ") - "
                                + bundle.getString("tReportFileTitle") + ".pdf\"");
                        reportService.generateTLReport(tl, response.getOutputStream());
                    } else {
                        response.setStatus(HttpStatus.NOT_FOUND.value());
                    }
                } catch (Exception e) {
                    LOGGER.error("An error occured while generating pdf : " + e.getMessage(), e);
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    @RequestMapping(value = "/cleanAndRunAllRules")
    public @ResponseBody ServiceResponse<String> cleanAndRunAllRules(@RequestBody TLCookie cookie) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStoreOrProd(cookie.getTlId(), cookie.getCookie())) {
                TL currentTl = tlService.getTL(cookie.getTlId());
                rulesRunner.runAllRulesByTL(currentTl);
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
