package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQResult;
import eu.europa.ec.joinup.tsl.business.dto.sieQ.SieQValidationForm;
import eu.europa.ec.joinup.tsl.business.service.SieQValidationService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/sieQValidation")
public class ApiSieValidationController {

    @Autowired
    private SieQValidationService sieQValidationService;

    @RequestMapping(value = "/validation/file", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<SieQResult> validateCertificate(@RequestBody MultipartFile certificateFile) {
        ServiceResponse<SieQResult> response = new ServiceResponse<>();
        response.setContent(sieQValidationService.getMatchingQualifiers(new SieQValidationForm(certificateFile)));
        response.setResponseStatus(HttpStatus.OK.toString());
        return response;
    }

    @RequestMapping(value = "/validation/b64", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<SieQResult> validateB64(@RequestBody String certificateB64) {
        ServiceResponse<SieQResult> response = new ServiceResponse<>();
        response.setContent(sieQValidationService.getMatchingQualifiers(new SieQValidationForm(certificateB64)));
        response.setResponseStatus(HttpStatus.OK.toString());
        return response;
    }

}
