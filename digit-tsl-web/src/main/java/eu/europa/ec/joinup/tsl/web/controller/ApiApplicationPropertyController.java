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

import eu.europa.ec.joinup.tsl.business.service.ApplicationPropertyService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBApplicationProperty;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/applicationProperties")
public class ApiApplicationPropertyController {

    @Autowired
    private ApplicationPropertyService propertyService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ServiceResponse<List<DBApplicationProperty>> getAudit() {
        ServiceResponse<List<DBApplicationProperty>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(propertyService.getBooleanProperties());
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/switch", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<DBApplicationProperty> switchProperty(@RequestBody DBApplicationProperty dbProperty) {
        ServiceResponse<DBApplicationProperty> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            dbProperty = propertyService.switchProperty(dbProperty);
            if (dbProperty == null) {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
            } else {
                response.setContent(dbProperty);
                response.setResponseStatus(HttpStatus.OK.toString());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
