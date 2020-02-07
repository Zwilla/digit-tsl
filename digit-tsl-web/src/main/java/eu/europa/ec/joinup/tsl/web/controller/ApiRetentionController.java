package eu.europa.ec.joinup.tsl.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.data.retention.CronRetention;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.DraftStoreRetentionDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.RetentionCriteriaDTO;
import eu.europa.ec.joinup.tsl.business.dto.data.retention.TrustedListRetentionDTO;
import eu.europa.ec.joinup.tsl.business.service.RetentionService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/retention")
public class ApiRetentionController {

    @Autowired
    private RetentionService retentionService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<List<DraftStoreRetentionDTO>> searchRetention(@RequestBody RetentionCriteriaDTO retentionCriteria) {
        ServiceResponse<List<DraftStoreRetentionDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                response.setContent(retentionService.searchRetentionData(retentionCriteria));
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (IllegalStateException e) {
                response.setErrorMessage(e.getMessage());
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/clean_draftstore", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> deleteDraftstore(@RequestBody DraftStoreRetentionDTO draftStore) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            retentionService.cleanDraftStore(draftStore);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/clean_trustedlist", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> deleteTL(@RequestBody TrustedListRetentionDTO trustedList) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            retentionService.cleanTrustedlist(trustedList);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/get_cron_retention", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<CronRetention> getCronRetention() {
        ServiceResponse<CronRetention> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                response.setContent(retentionService.searchNextCronRetention());
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (IllegalStateException e) {
                response.setErrorMessage(e.getMessage());
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
