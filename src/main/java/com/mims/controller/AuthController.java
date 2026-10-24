package com.mims.controller;

import com.mims.auth.MimsUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping({"/", "/login", "/Login/Login.jsp"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal MimsUserPrincipal user, Model model) {
        model.addAttribute("userId", user.getUsername());
        model.addAttribute("userRole", user.getUserType());
        return "home";
    }
}
