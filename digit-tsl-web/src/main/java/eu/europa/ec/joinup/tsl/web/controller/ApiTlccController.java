package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.service.TLCCService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/tlcc")
public class ApiTlccController {

    @Autowired
    private TLCCService tlccService;

    @Autowired
    private UserService userService;

    @Value("${tlcc.active}")
    private Boolean tlccActive;

    @RequestMapping(value = "/getEtsiPortalUrl")
    public @ResponseBody ServiceResponse<String> getEtsiPortalUrl() {
        ServiceResponse<String> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            response.setResponseStatus(HttpStatus.OK.toString());
            response.setContent(tlccService.getEtsiPortalUrl());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/isActive")
    public @ResponseBody ServiceResponse<Boolean> isActive() {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setResponseStatus(HttpStatus.OK.toString());
            response.setContent(tlccActive);
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
