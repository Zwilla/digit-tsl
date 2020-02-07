package eu.europa.ec.joinup.tsl.web.controller;

import java.util.List;

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

import eu.europa.ec.joinup.tsl.business.dto.NewDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.Audit;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.audit.AuditSearchDTO;
import eu.europa.ec.joinup.tsl.business.service.AuditService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/audit")
public class ApiAuditController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<Audit>> getAudit() {
        ServiceResponse<List<Audit>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
                response.setContent(auditService.getAllAuditOrderByDateDesc());
                response.setResponseStatus(HttpStatus.OK.toString());
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/criteriaList", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<AuditCriteriaDTO> getCriteria() {
        ServiceResponse<AuditCriteriaDTO> response = new ServiceResponse<>();
        response.setResponseStatus(HttpStatus.OK.toString());
        response.setContent(auditService.initCriteria());
        return response;
    }

    @RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<List<Audit>> searchAudit(@RequestBody AuditSearchDTO searchDTO) {
        ServiceResponse<List<Audit>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(auditService.searchAuditByCriteria(searchDTO));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<NewDTO>> getNews() {
        ServiceResponse<List<NewDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(auditService.getLatestNews());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/all_news", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<List<NewDTO>> getAllNews(@RequestBody AuditSearchDTO searchDTO) {
        ServiceResponse<List<NewDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(auditService.getFilteredNews(searchDTO));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/{xmlId}/get_tl_id", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<Integer> getTLId(@PathVariable("xmlId") int fileId) {
        ServiceResponse<Integer> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(auditService.findTlIDByAuditXmlFileId(fileId));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
