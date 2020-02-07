package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.europa.ec.joinup.tsl.business.service.MessagesService;

@Controller
public class ApiMessageController {

    @Autowired
    private MessagesService messageService;

    @RequestMapping("/js/message.js")
    public ResponseEntity<String> loadScript() {

        StringBuilder sb = new StringBuilder();
        sb.append("function initMessages($scope) {\n");
        sb.append(messageService.getMessagesBundle());
        sb.append("}");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/javascript"));

        return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
    }

}
