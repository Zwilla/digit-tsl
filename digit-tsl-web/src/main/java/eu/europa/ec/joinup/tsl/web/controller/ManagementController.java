package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.DBUser;

@Controller
@RequestMapping(value = "/management")
public class ManagementController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userManagement", method = RequestMethod.GET)
    public String tl(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "userManagement";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/dataProperties" }, method = RequestMethod.GET)
    public String dataProperties(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataProperties";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/dataChecks" }, method = RequestMethod.GET)
    public String dataChecks(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataChecks";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/signingCertificate" }, method = RequestMethod.GET)
    public String keyStore(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "signingCertificate";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/system" }, method = RequestMethod.GET)
    public String system(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "system";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/audit" }, method = RequestMethod.GET)
    public String audit(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "audit";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/dataRetention" }, method = RequestMethod.GET)
    public String dataRetention(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "dataRetention";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/cronRetention" }, method = RequestMethod.GET)
    public String retentionView(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "cronRetention";
            }
        }
        return "denied";
    }

    @RequestMapping(value = { "/logs" }, method = RequestMethod.GET)
    public String logs(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            DBUser userConnected = userService.getDBUser(SecurityContextHolder.getContext().getAuthentication().getName());
            if ((userConnected != null) && userService.isManagement(userConnected.getEcasId())) {
                return "application_logs";
            }
        }
        return "denied";
    }

}
