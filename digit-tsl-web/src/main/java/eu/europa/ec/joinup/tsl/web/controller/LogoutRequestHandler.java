package eu.europa.ec.joinup.tsl.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LogoutRequestHandler {

    @Value("${casServerUrl}")
    private String casServerUrl;

    @RequestMapping(value = "/end-session", method = RequestMethod.GET)
    @ResponseBody
    public void logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getSession().invalidate();
        resp.sendRedirect(casServerUrl + "/logout");
    }

}
