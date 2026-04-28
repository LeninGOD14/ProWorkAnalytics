package com.prowork.analytics.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/redirect")
    public String redirectByRole(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {

            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin";
            }

            if (authority.getAuthority().equals("ROLE_SUPERVISOR")) {
                return "redirect:/supervisor";
            }

            if (authority.getAuthority().equals("ROLE_OPERARIO")) {
                return "redirect:/operario";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/conocer")
    public String conocer() {
        return "conocer";
    }
}
