package com.example.catering_system.customer.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.catering_system.customer.model.Customer;
import com.example.catering_system.customer.model.Order;
import com.example.catering_system.customer.service.CustomerService;
import com.example.catering_system.customer.service.OrderService;

/**
 * Web Controller for serving HTML pages and dashboard navigation
 */
@Controller
public class WebController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private OrderService orderService;
    
    
    /**
     * Customer Management Page
     */
    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("pageTitle", "Customer Management");
        
        try {
            // Try to get real data from database
            List<Customer> customers = customerService.findAllCustomers();
            model.addAttribute("customers", customers);
            System.out.println("Loaded " + customers.size() + " customers from database");
        } catch (Exception e) {
            // Fallback to sample data if database fails
            System.out.println("Database error, using sample data: " + e.getMessage());
            List<Customer> sampleCustomers = createSampleCustomers();
            model.addAttribute("customers", sampleCustomers);
        }
        
        return "customers";
    }
    
    private List<Customer> createSampleCustomers() {
        List<Customer> customers = new ArrayList<>();
        
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFullName("John Smith");
        customer1.setEmail("john.smith@email.com");
        customer1.setPhone("+1-555-0101");
        customer1.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        customer1.setStatus(Customer.CustomerStatus.ACTIVE);
        customer1.setTotalSpent(java.math.BigDecimal.valueOf(450.00));
        customer1.setLoyaltyPoints(java.math.BigDecimal.valueOf(2250.00));
        customers.add(customer1);
        
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFullName("Sarah Johnson");
        customer2.setEmail("sarah.johnson@email.com");
        customer2.setPhone("+1-555-0201");
        customer2.setCustomerType(Customer.CustomerType.INDIVIDUAL);
        customer2.setStatus(Customer.CustomerStatus.ACTIVE);
        customer2.setTotalSpent(java.math.BigDecimal.valueOf(320.00));
        customer2.setLoyaltyPoints(java.math.BigDecimal.valueOf(1600.00));
        customers.add(customer2);
        
        Customer customer3 = new Customer();
        customer3.setId(3L);
        customer3.setFullName("Michael Chen");
        customer3.setEmail("michael.chen@email.com");
        customer3.setPhone("+1-555-0301");
        customer3.setCustomerType(Customer.CustomerType.CORPORATE);
        customer3.setStatus(Customer.CustomerStatus.VIP);
        customer3.setTotalSpent(java.math.BigDecimal.valueOf(890.00));
        customer3.setLoyaltyPoints(java.math.BigDecimal.valueOf(4450.00));
        customers.add(customer3);
        
        return customers;
    }
    
    /**
     * Order Management Page
     */
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("pageTitle", "Order Management");
        try {
            List<Order> orders = orderService.findAllOrders();
            model.addAttribute("orders", orders);
            // Add customers for order creation dropdown
            List<Customer> customers = customerService.findAllCustomers();
            model.addAttribute("customers", customers);
            System.out.println("Loaded " + orders.size() + " orders from database");
            
            // Debug: Print order details
            for (Order order : orders) {
                System.out.println("Order: " + order.getOrderNumber() + " - " + order.getStatus());
            }
            
        } catch (Exception e) {
            model.addAttribute("orders", List.of());
            System.out.println("Error loading orders: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }
        return "orders-customer";
    }
    
    
    /**
     * Reports and Analytics Page
     */
    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("pageTitle", "Reports & Analytics");
        
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastWeek = now.minusWeeks(1);
            LocalDateTime lastMonth = now.minusMonths(1);
            LocalDateTime lastYear = now.minusYears(1);
            
            // Revenue Statistics
            var revenueStats = orderService.getRevenueStatistics();
            model.addAttribute("revenueStats", revenueStats);
            
            // Time-based Analytics
            model.addAttribute("ordersThisWeek", orderService.getOrdersSince(lastWeek));
            model.addAttribute("ordersThisMonth", orderService.getOrdersSince(lastMonth));
            model.addAttribute("ordersThisYear", orderService.getOrdersSince(lastYear));
            
            model.addAttribute("revenueThisWeek", orderService.getTotalRevenueAllSince(lastWeek));
            model.addAttribute("revenueThisMonth", orderService.getTotalRevenueAllSince(lastMonth));
            model.addAttribute("revenueThisYear", orderService.getTotalRevenueAllSince(lastYear));
            
            model.addAttribute("avgOrderValue", orderService.getAverageOrderValueSince(lastMonth));
            
            // Customer Statistics
            model.addAttribute("totalCustomers", customerService.getTotalCustomers());
            model.addAttribute("activeCustomers", customerService.getCustomerCountByStatus(Customer.CustomerStatus.ACTIVE));
            model.addAttribute("newCustomersThisMonth", customerService.getNewCustomersSince(lastMonth));
            
            // Top Performers
            model.addAttribute("topCustomersBySpending", customerService.getTopCustomersBySpending(5));
            model.addAttribute("topCustomersByLoyalty", customerService.getTopCustomersByLoyalty(5));
            model.addAttribute("topOrdersByValue", orderService.getTopOrdersByValueSince(lastMonth, 5));
            
            // Distribution Statistics
            model.addAttribute("customerTypeStats", customerService.getCustomerTypeStats());
            model.addAttribute("customerStatusStats", customerService.getCustomerStatusStats());
            model.addAttribute("orderTypeStats", orderService.getOrderTypeStatsSince(lastMonth));
            model.addAttribute("orderStatusStats", orderService.getOrderStatusStatsSince(lastMonth));
            
            // Active Orders
            model.addAttribute("activeOrders", orderService.getActiveOrdersCount());
            
        } catch (Exception e) {
            // Fallback to default values if database fails
            model.addAttribute("revenueStats", null);
            model.addAttribute("totalCustomers", 0);
            model.addAttribute("activeCustomers", 0);
            model.addAttribute("activeOrders", 0);
            model.addAttribute("ordersThisWeek", 0);
            model.addAttribute("ordersThisMonth", 0);
            model.addAttribute("revenueThisWeek", 0.0);
            model.addAttribute("revenueThisMonth", 0.0);
            model.addAttribute("avgOrderValue", 0.0);
            model.addAttribute("error", "Unable to load analytics data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "reports-customer";
    }
    
    
    /**
     * Support and Help Page
     */
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("pageTitle", "Support & Help");
        return "support-customer";
    }
    
    /**
     * Database View Page (H2 Console access)
     */
    @GetMapping("/database")
    public String database(Model model) {
        model.addAttribute("pageTitle", "Database Console");
        model.addAttribute("h2ConsoleUrl", "/h2-console");
        return "database";
    }
    
    /**
     * Support Tickets Page
     */
    @GetMapping("/support-tickets")
    public String supportTickets(Model model) {
        model.addAttribute("pageTitle", "Support Tickets");
        return "customer-care/support-tickets";
    }
    
    /**
     * Database Test Page - for debugging
     */
    @GetMapping("/test-db")
    public String testDatabase(Model model) {
        model.addAttribute("pageTitle", "Database Test");
        
        try {
            // Test customers
            List<Customer> customers = customerService.findAllCustomers();
            model.addAttribute("customerCount", customers.size());
            model.addAttribute("customers", customers);
            
            // Test orders
            List<Order> orders = orderService.findAllOrders();
            model.addAttribute("orderCount", orders.size());
            model.addAttribute("orders", orders);
            
            System.out.println("Database Test - Customers: " + customers.size() + ", Orders: " + orders.size());
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
        }
        
        return "test";
    }
}
