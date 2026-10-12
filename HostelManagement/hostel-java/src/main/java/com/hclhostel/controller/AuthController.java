package com.hclhostel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "redirect:/student/login";
    }

    @GetMapping("/admin/login")
    public String adminLogin(@RequestParam(required = false) String error,
                             @RequestParam(required = false) String logout,
                             Model model) {
        model.addAttribute("role", "ADMIN");
        model.addAttribute("roleLabel", "Admin");
        if (error != null) model.addAttribute("error", errorMessage(error));
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping("/warden/login")
    public String wardenLogin(@RequestParam(required = false) String error,
                              @RequestParam(required = false) String logout,
                              Model model) {
        model.addAttribute("role", "WARDEN");
        model.addAttribute("roleLabel", "Warden");
        if (error != null) model.addAttribute("error", errorMessage(error));
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping("/student/login")
    public String studentLogin(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String logout,
                               Model model) {
        model.addAttribute("role", "STUDENT");
        model.addAttribute("roleLabel", "Student");
        if (error != null) model.addAttribute("error", errorMessage(error));
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    private String errorMessage(String error) {
        return switch (error) {
            case "bad" -> "Invalid username or password.";
            case "inactive" -> "This account is inactive.";
            case "role" -> "Role mismatch: you cannot log in through this page.";
            default -> "Login failed.";
        };
    }
}
