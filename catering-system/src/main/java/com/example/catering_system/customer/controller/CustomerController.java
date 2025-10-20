package com.example.catering_system.customer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.catering_system.customer.model.Customer;
import com.example.catering_system.customer.service.CustomerService;

/**
 * REST API Controller for Customer Management
 * Provides full CRUD operations for customers
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private CustomerService customerService;
    
    /**
     * Get all customers
     * GET /api/customers
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.findAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get customer by ID
     * GET /api/customers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            Optional<Customer> customer = customerService.findById(id);
            if (customer.isPresent()) {
                return ResponseEntity.ok(customer.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get customer by email
     * GET /api/customers/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        try {
            Optional<Customer> customer = customerService.findByEmail(email);
            if (customer.isPresent()) {
                return ResponseEntity.ok(customer.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Create a new customer
     * POST /api/customers
     */
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating customer: " + e.getMessage());
        }
    }
    
    /**
     * Update an existing customer
     * PUT /api/customers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        try {
            // Ensure the ID matches the path variable
            customer.setId(id);
            Customer updatedCustomer = customerService.updateCustomer(customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating customer: " + e.getMessage());
        }
    }
    
    /**
     * Delete a customer (soft delete - sets status to INACTIVE)
     * DELETE /api/customers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting customer: " + e.getMessage());
        }
    }
    
    /**
     * Get customers by status
     * GET /api/customers/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Customer>> getCustomersByStatus(@PathVariable Customer.CustomerStatus status) {
        try {
            List<Customer> customers = customerService.findCustomersByStatus(status);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get high-value customers (total spent >= amount)
     * GET /api/customers/high-value?amount=1000
     */
    @GetMapping("/high-value")
    public ResponseEntity<List<Customer>> getHighValueCustomers(@RequestParam(defaultValue = "1000") Double amount) {
        try {
            List<Customer> customers = customerService.findHighValueCustomers(java.math.BigDecimal.valueOf(amount));
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get loyal customers (loyalty points >= points)
     * GET /api/customers/loyal?points=100
     */
    @GetMapping("/loyal")
    public ResponseEntity<List<Customer>> getLoyalCustomers(@RequestParam(defaultValue = "100") Double points) {
        try {
            List<Customer> customers = customerService.findLoyalCustomers(java.math.BigDecimal.valueOf(points));
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
