package com.hclhostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) model.addAttribute("error", errorMessage(error));
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping({"/admin/login", "/warden/login", "/student/login"})
    public String legacyLogin() {
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    private String errorMessage(String error) {
        return switch (error) {
            case "bad" -> "Invalid username or password.";
            case "inactive" -> "This account is inactive.";
            case "unsupported" -> "This account does not have an available dashboard. Contact the administrator.";
            default -> "Login failed.";
        };
    }
}
