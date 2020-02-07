package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

    @RequestMapping(value = "/401", method = RequestMethod.GET)
    public String error401(Model model) {
        return "error401";
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error404(Model model) {
        return "error404";
    }

}
