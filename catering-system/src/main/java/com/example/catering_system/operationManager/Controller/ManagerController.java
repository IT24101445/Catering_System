package com.example.catering_system.operationManager.Controller;

import com.example.catering_system.operationManager.Entity.Manager;
import com.example.catering_system.operationManager.Service.SimpleDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Changed from javax.servlet to jakarta.servlet
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/operation")
public class ManagerController {

    @Autowired
    private SimpleDatabaseService dbService;

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "Operation/login-operation";
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage() {
        return "Operation/register-operation";
    }

    // Process registration
    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                          @RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String email,
                          @RequestParam String phone,
                          Model model) {
        // Add registration logic here if needed
        model.addAttribute("message", "Registration successful! Please login.");
        return "Operation/login-operation";
    }

    // Process login
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        System.out.println("=== CONTROLLER LOGIN DEBUG ===");
        System.out.println("Received login request for username: " + username);
        System.out.println("Password length: " + (password != null ? password.length() : "NULL"));
        
        Manager manager = dbService.validateManager(username, password);
        if (manager != null) {
            System.out.println("Login successful, redirecting to dashboard");
            session.setAttribute("manager", manager);
            return "redirect:/operation/dashboard";
        } else {
            System.out.println("Login failed, showing error message");
            model.addAttribute("error", "Invalid username or password");
            return "Operation/login-operation";
        }
    }

    // Show dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        if (manager == null) {
            return "redirect:/operation/login";
        }

        // Add session messages to model and clear them
        if (session.getAttribute("successMessage") != null) {
            model.addAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }
        if (session.getAttribute("errorMessage") != null) {
            model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        model.addAttribute("orders", dbService.getPendingOrders());
        model.addAttribute("staffList", dbService.getAvailableStaff());
        return "Operation/dashboard-operation";
    }

    // Assign order to staff
    @PostMapping("/assign")
    public String assignOrder(@RequestParam int orderId, @RequestParam int staffId,
                              HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        if (manager == null) {
            return "redirect:/operation/login";
        }

        boolean success = dbService.assignOrder(orderId, staffId);
        if (success) {
            dbService.sendNotification("Order " + orderId + " assigned to staff " + staffId);
            session.setAttribute("successMessage", "Order " + orderId + " has been successfully assigned!");
        } else {
            session.setAttribute("errorMessage", "Failed to assign order. Please try again.");
        }
        return "redirect:/operation/dashboard";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/operation/login";
    }

    // Show assigned orders
    @GetMapping("/assigned-orders")
    public String assignedOrders(HttpSession session, Model model) {
        Manager manager = (Manager) session.getAttribute("manager");
        if (manager == null) {
            return "redirect:/operation/login";
        }

        System.out.println("=== ASSIGNED ORDERS DEBUG ===");
        var assignedOrders = dbService.getAssignedOrders();
        System.out.println("Found " + assignedOrders.size() + " assigned orders");
        for (var order : assignedOrders) {
            System.out.println("Order: " + order.getOrderId() + " - " + order.getCustomerName() + " - " + order.getStaffName());
        }
        
        model.addAttribute("assignedOrders", assignedOrders);
        return "Operation/assignedOrders-operation";
    }
    // Create new order
    @PostMapping("/orders/create")
    public String createOrder(@RequestParam String customerName,
                              @RequestParam String details,
                              HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.createOrder(customerName, details);
        return "redirect:/operation/dashboard";
    }

    // Delete order
    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.deleteOrder(id);
        return "redirect:/operation/dashboard";
    }

    // Add new staff
    @PostMapping("/staff/add")
    public String addStaff(@RequestParam String name,
                           @RequestParam String role,
                           HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.createStaff(name, role);
        return "redirect:/operation/dashboard";
    }

    // Update staff availability
    @PostMapping("/staff/availability")
    public String updateStaffAvailability(@RequestParam int id,
                                          @RequestParam boolean available,
                                          HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.updateStaffAvailability(id, available);
        return "redirect:/operation/dashboard";
    }

    // Delete staff member
    @GetMapping("/staff/delete/{id}")
    public String deleteStaff(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.deleteStaff(id);
        return "redirect:/operation/dashboard";
    }

    // Delete assigned order (with booking cleanup)
    @GetMapping("/assigned-orders/delete/{orderId}")
    public String deleteAssignedOrder(@PathVariable int orderId, HttpSession session) {
        if (session.getAttribute("manager") == null) {
            return "redirect:/operation/login";
        }
        dbService.deleteAssignedOrder(orderId);
        return "redirect:/operation/assigned-orders";
    }
}