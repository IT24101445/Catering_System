package com.example.catering_system.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.catering_system.customer.model.User;
import com.example.catering_system.customer.service.UserService;

/**
 * Settings Controller for system configuration and user management
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String settings(Model model, Authentication authentication) {
        model.addAttribute("pageTitle", "System Settings");
        
        try {
            // Get current user info
            if (authentication != null && authentication.isAuthenticated()) {
                String currentUsername = authentication.getName();
                User currentUser = userService.findByUsername(currentUsername).orElse(null);
                model.addAttribute("currentUser", currentUser);
            }
            
            // Get system statistics
            model.addAttribute("totalUsers", userService.getTotalUsers());
            model.addAttribute("activeUsers", userService.getActiveUsersCount());
            
            // Get all users for management
            List<User> allUsers = userService.findAllUsers();
            model.addAttribute("allUsers", allUsers);
            
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load settings data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "settings-customer";
    }
    
    @PostMapping("/update-profile")
    public String updateProfile(@RequestParam Long userId,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String phone,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(userId, fullName, email, phone);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        
        return "redirect:/settings";
    }
    
    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long userId,
                                @RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to change password: " + e.getMessage());
        }
        
        return "redirect:/settings";
    }
    
    @PostMapping("/toggle-user-status")
    public String toggleUserStatus(@RequestParam Long userId,
                                  RedirectAttributes redirectAttributes) {
        try {
            User user = userService.toggleUserStatus(userId);
            String status = user.getIsActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("success", "User " + user.getUsername() + " has been " + status + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user status: " + e.getMessage());
        }
        
        return "redirect:/settings";
    }
    
    @PostMapping("/create-user")
    public String createUser(@RequestParam String username,
                            @RequestParam String password,
                            @RequestParam String fullName,
                            @RequestParam String email,
                            @RequestParam String phone,
                            RedirectAttributes redirectAttributes) {
        try {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setIsActive(true);
            
            userService.createUser(newUser);
            redirectAttributes.addFlashAttribute("success", "User " + username + " created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
        }
        
        return "redirect:/settings";
    }
}
