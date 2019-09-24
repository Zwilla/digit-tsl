/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
 *  
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
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
