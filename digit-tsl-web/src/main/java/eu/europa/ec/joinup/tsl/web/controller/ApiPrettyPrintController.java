package eu.europa.ec.joinup.tsl.web.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.business.service.TLPrettyPrintService;
import eu.europa.ec.joinup.tsl.business.service.TLService;

@Controller
@RequestMapping(value = "/api/pretty")
public class ApiPrettyPrintController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPrettyPrintController.class);

    @Autowired
    private TLService tlService;

    @Autowired
    private TLPrettyPrintService tlPrettyPrintService;

    @Value("${tsl.folder}")
    private String folderPath;

    @RequestMapping(value = "/download/{tlId}", method = RequestMethod.GET)
    public void download(@PathVariable int tlId, HttpSession session, HttpServletResponse response) {
        TL tl = tlService.getTL(tlId);
        if (tl == null) {
            LOGGER.error("TL " + tlId + " not found.");
            return;
        }
        String pdfPath = tlPrettyPrintService.triggerPrettyPrint(tlId);
        if (pdfPath != null) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(folderPath + File.separator + pdfPath);
                response.setContentType(eu.europa.esig.dss.MimeType.PDF.getMimeTypeString());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + tl.getDbName() + " - PrettyPrint.pdf\"");

                IOUtils.copy(fis, response.getOutputStream());
            } catch (Exception e) {
                LOGGER.error("An error occured while generating pdf : " + e.getMessage(), e);
            }

        } else {
            LOGGER.error("An error occured while generating pdf.");
        }
    }
}
