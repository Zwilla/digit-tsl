package eu.europa.ec.joinup.tsl.web.controller;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europa.ec.joinup.tsl.business.dto.LogFileDTO;
import eu.europa.ec.joinup.tsl.business.service.LogManagerService;
import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.web.form.ServiceResponse;

@Controller
@RequestMapping(value = "/api/logs")
public class ApiLogsController {

    @Autowired
    private LogManagerService logManagerService;

    @Autowired
    private UserService userService;

    private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @RequestMapping(value = "/get_all", method = RequestMethod.GET)
    public @ResponseBody ServiceResponse<List<LogFileDTO>> getAllLogs() {
        ServiceResponse<List<LogFileDTO>> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            response.setResponseStatus(HttpStatus.OK.toString());
            response.setContent(logManagerService.getAllLogs());
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }
        return response;
    }

    @RequestMapping(value = "/download_file/{fileName}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            byte[] file = logManagerService.downloadFile(fileName);
            if (file != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(new MediaType("application", "xml", Charsets.UTF_8));
                headers.set("Content-Disposition", "attachment; filename=" + fileName);
                ResponseEntity<byte[]> resp = new ResponseEntity<>(file, headers, HttpStatus.OK);
                return resp;
            } else {
                throw new IllegalStateException(bundle.getString("error.log.not.found").replaceAll("%FILE_NAME%", fileName));
            }
        } else {
            throw new IllegalStateException(bundle.getString("user.action.not.authorized"));
        }

    }

    @RequestMapping(value = "/delete_file", method = RequestMethod.POST)
    public @ResponseBody ServiceResponse<Boolean> deleteFile(@RequestBody String fileName) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isManagement(SecurityContextHolder.getContext().getAuthentication().getName())) {
            try {
                response.setContent(logManagerService.deleteFile(fileName));
                response.setResponseStatus(HttpStatus.OK.toString());
            } catch (Exception e) {
                response.setResponseStatus(HttpStatus.BAD_REQUEST.toString());
                response.setErrorMessage(e.getMessage());
            }
        } else {
            response.setResponseStatus(HttpStatus.UNAUTHORIZED.toString());
        }

        return response;
    }

}
