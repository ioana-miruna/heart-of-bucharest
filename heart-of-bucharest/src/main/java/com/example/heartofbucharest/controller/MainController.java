package com.example.heartofbucharest.controller;

import com.example.heartofbucharest.model.User;
import com.example.heartofbucharest.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;

    // Home page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Show the sign-up page
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "An account with this email already exists.");
            return "signup";
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration: " + e.getMessage());
            return "signup";
        }

        model.addAttribute("message", "Registration successful! You can now log in.");
        return "login";
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null) {
            model.addAttribute("error", "No account found with this email. Please sign up.");
            return "login";
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            model.addAttribute("error", "Invalid password. Please try again.");
            return "login";
        }

        return "redirect:/";
    }

    // Opportunities page
    @GetMapping("/opportunities")
    public String showOpportunities() {
        return "opportunities";
    }

    // About page
    @GetMapping("/about")
    public String showAbout() {
        return "about";
    }

    // Contact page
    @GetMapping("/contact")
    public String showContact() {
        return "contact";
    }
}
