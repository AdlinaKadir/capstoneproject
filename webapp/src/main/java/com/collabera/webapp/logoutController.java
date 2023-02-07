package com.collabera.webapp;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/logout")
public class logoutController {
    @RequestMapping(method= RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
