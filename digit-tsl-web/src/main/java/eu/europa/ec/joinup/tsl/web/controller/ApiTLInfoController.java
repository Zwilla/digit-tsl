package eu.europa.ec.joinup.tsl.web.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.service.TLDataService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/tl/information")
public class ApiTLInfoController {

    @Autowired
    private TLDataService tlDataService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/taken-over-by/{country}", method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<Set<String>> getTOBByCountry(@PathVariable("country") String country) {
        ServiceResponse<Set<String>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setContent(tlDataService.findCountryTSPNames(country));
            response.setResponseStatus(HttpStatus.OK.toString());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

}
