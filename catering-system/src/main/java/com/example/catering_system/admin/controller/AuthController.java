package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.User;
import com.example.catering_system.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller("adminAuthController")
public class AuthController {

    @Autowired
    private AdminUserService userService;

    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "admin/login";
    }

    @GetMapping("/admin/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/register";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            return "redirect:/admin/home";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/admin/login";
        }
    }

    @PostMapping("/admin/register")
    public String register(User user, 
                          @RequestParam String confirmPassword,
                          RedirectAttributes redirectAttributes) {
        
        // Check if passwords match
        if (!user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/admin/register";
        }

        // Check if username already exists
        if (userService.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
            return "redirect:/admin/register";
        }

        // Check if email already exists
        if (userService.existsByEmail(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/admin/register";
        }

        // Save user
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/admin/login";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
