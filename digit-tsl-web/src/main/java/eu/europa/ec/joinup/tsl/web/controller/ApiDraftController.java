package eu.europa.ec.joinup.tsl.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import eu.europa.ec.joinup.tsl.business.dto.TrustedListsReport;
import eu.europa.ec.joinup.tsl.business.dto.merge.TLMergeResultDTO;
import eu.europa.ec.joinup.tsl.business.service.DraftStoreService;
import eu.europa.ec.joinup.tsl.business.service.MergeDraftService;
import eu.europa.ec.joinup.tsl.business.service.TLDraftService;
import eu.europa.ec.joinup.tsl.business.service.TLService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBTrustedLists;
import eu.europa.ec.joinup.tsl.web.form.PerformMergeDTO;
import eu.europa.ec.joinup.tsl.web.form.PrepareMergeDTO;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;
//import eu.europa.ec.joinup.tsl.web.form.TLDraftDelete;
import eu.europa.ec.joinup.tsl.web.form.TLDraftDelete;

@Controller
@SessionAttributes(value = { "mergeChanges" })
@RequestMapping(value = "/api/draft")
public class ApiDraftController {

    @Autowired
    private TLDraftService draftService;

    @Autowired
    private DraftStoreService draftStoreService;

    @Autowired
    private TLService tlService;

    @Autowired
    private MergeDraftService mergeDraftService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/createDraftStore", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<String> createDraftStore() {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            response.setContent(draftStoreService.getNewDraftStore());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/checkDraftStore/{draftStoreId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResponse<Boolean> createDraftStore(@PathVariable String draftStoreId) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            Boolean validity = draftStoreService.checkDraftStoreId(draftStoreId);
            response.setContent(validity);
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<String> deleteDraft(@RequestBody TLDraftDelete tlDraftDelete) {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (tlService.inStoreOrProd(tlDraftDelete.getTlId(), tlDraftDelete.getCookie())) {
                try {
                    tlService.detachedNotification(tlDraftDelete.getTlId(), tlDraftDelete.getRejected());
                    tlService.deleteDraft(tlDraftDelete.getTlId());
                    response.setResponseStatus(HttpStatus.OK.toString());
                } catch (Exception e) {
                    response.setResponseStatus(HttpStatus.NOT_FOUND.toString());
                }
            } else {
                response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/checkNotification/{draftId}", method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<Boolean> checkNotification(@PathVariable int draftId) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(tlService.checkDraftNotification(draftId));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/merge/changes", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TLMergeResultDTO> getMergeChanges(@RequestBody PrepareMergeDTO mergeDTO, Model model, HttpSession session) {
        session.removeAttribute("mergeChanges");
        ServiceResponse<TLMergeResultDTO> response = new ServiceResponse<>();
        response.setResponseStatus(HttpStatus.OK.toString());
        TLMergeResultDTO mergeChanges = mergeDraftService.getTLMergeResult(mergeDTO.getCountryCode(), mergeDTO.getDrafts());
        model.addAttribute("mergeChanges", mergeChanges);
        response.setContent(mergeChanges);
        return response;
    }

    @RequestMapping(value = "/merge/perform", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<TrustedListsReport> performMerge(@RequestBody PerformMergeDTO performMergeDTO, HttpSession session,
            @ModelAttribute("mergeChanges") TLMergeResultDTO mergeChanges) {
        ServiceResponse<TrustedListsReport> response = new ServiceResponse<>();
        DBTrustedLists mergedDraft = mergeDraftService.mergeDrafts(performMergeDTO.getCountryCode(), performMergeDTO.getCookie(), mergeChanges);
        response.setContent(draftService.finalizeDraftCreation(mergedDraft, SecurityContextHolder.getContext().getAuthentication().getName()));
        response.setResponseStatus(HttpStatus.OK.toString());
        session.removeAttribute("mergeChanges");
        return response;
    }
}
