package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/wizard")
public class WizardController {

    @RequestMapping(value = "/signingCertificate", method = RequestMethod.GET)
    public String signingCertificate(Model model) {
        return "wizard/wizardSigningCertificate";
    }
    
    @RequestMapping(value = "/sieqExtension", method = RequestMethod.GET)
    public String sieqExtension(Model model) {
        return "wizard/wizardSieQExtension";
    }
    
    @RequestMapping(value = "/sieqGuidelines", method = RequestMethod.GET)
    public String wizardSieQGuidelines(Model model) {
        return "wizard/wizardSieQGuidelines";
    }

}
