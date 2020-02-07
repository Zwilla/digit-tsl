package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TlController {

    @RequestMapping(value = "/tl/{id}", method = RequestMethod.GET)
    public String tl(Model model, @PathVariable String id) {
        return "tlBrowser";
    }

}
