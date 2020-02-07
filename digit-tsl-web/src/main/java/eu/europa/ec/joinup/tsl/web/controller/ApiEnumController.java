package eu.europa.ec.joinup.tsl.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.europa.ec.joinup.tsl.business.service.UserService;
import eu.europa.ec.joinup.tsl.model.enums.CheckImpact;
import eu.europa.ec.joinup.tsl.model.enums.CheckStatus;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

@Controller
public class ApiEnumController {

    @Autowired
    private UserService userService;

    @RequestMapping("/js/tag.js")
    public ResponseEntity<String> loadScript() {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            StringBuilder sb = new StringBuilder();
            sb.append("function initScope($scope) {\n");
            for (Tag myTag : Tag.values()) {
                sb.append("$scope.TAG_");
                sb.append(myTag);
                sb.append(" = '");
                sb.append(myTag);
                sb.append("';\n");
            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/js/checkStatus.js")
    public ResponseEntity<String> loadCheckStatus() {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {

            StringBuilder sb = new StringBuilder();
            sb.append("function initStatusEnum($scope) {\n");
            sb.append("$scope.STATUS=[];\n");
            for (CheckStatus myEnum : CheckStatus.values()) {
                if (!myEnum.equals(CheckStatus.SUCCESS)) {
                    sb.append("$scope.STATUS.push(\"");
                    sb.append(myEnum);
                    sb.append("\");\n");
                }

            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/js/checkImpact.js")
    public ResponseEntity<String> loadCheckImpact() {
        if ((SecurityContextHolder.getContext().getAuthentication() != null) && userService.isAuthenticated(SecurityContextHolder.getContext().getAuthentication().getName())) {
            StringBuilder sb = new StringBuilder();
            sb.append("function initImpactEnum($scope) {\n");
            sb.append("$scope.IMPACT=[];\n");
            for (CheckImpact myEnum : CheckImpact.values()) {
                sb.append("$scope.IMPACT.push(\"");
                sb.append(myEnum);
                sb.append("\");\n");
            }
            sb.append("}");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/javascript"));

            return new ResponseEntity<>(sb.toString(), headers, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
