package com.example.catering_system.customer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catering_system.customer.model.Customer;
import com.example.catering_system.customer.repository.CustomerRepository;

/**
 * Service class for Customer management
 */
@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        customer.setRegistrationDate(LocalDateTime.now());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    
    public Customer updateCustomer(Customer customer) {
        // Check if customer exists
        Customer existingCustomer = customerRepository.findById(customer.getId())
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        
        // Update fields
        existingCustomer.setFullName(customer.getFullName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setCompanyName(customer.getCompanyName());
        existingCustomer.setCustomerType(customer.getCustomerType());
        existingCustomer.setStatus(customer.getStatus());
        existingCustomer.setPrimaryAddress(customer.getPrimaryAddress());
        existingCustomer.setNotes(customer.getNotes());
        existingCustomer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(existingCustomer);
    }
    
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.setStatus(Customer.CustomerStatus.INACTIVE);
        customerRepository.save(customer);
    }
    
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }
    
    public List<Customer> findCustomersByStatus(Customer.CustomerStatus status) {
        return customerRepository.findByStatus(status);
    }
    
    public List<Customer> findCustomersByType(Customer.CustomerType customerType) {
        return customerRepository.findByCustomerType(customerType);
    }
    
    public List<Customer> findHighValueCustomers(BigDecimal minAmount) {
        return customerRepository.findHighValueCustomers(minAmount);
    }
    
    public List<Customer> findLoyalCustomers(BigDecimal minPoints) {
        return customerRepository.findLoyalCustomers(minPoints);
    }
    
    public long getCustomerCountByStatus(Customer.CustomerStatus status) {
        return customerRepository.countByStatus(status);
    }
    
    public BigDecimal getTotalRevenueFromActiveCustomers() {
        return customerRepository.getTotalRevenueFromActiveCustomers();
    }
    
    public Customer addLoyaltyPoints(Long customerId, BigDecimal points) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.addLoyaltyPoints(points);
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
    
    public Customer updateTotalSpent(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        customer.addToTotalSpent(amount);
        customer.setLastOrderDate(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }
    
    public long getTotalCustomers() {
        return customerRepository.count();
    }
    
    // Analytics methods
    public List<Object[]> getCustomerTypeStats() {
        return customerRepository.getCustomerTypeStats();
    }
    
    public List<Object[]> getCustomerStatusStats() {
        return customerRepository.getCustomerStatusStats();
    }
    
    public List<Customer> getTopCustomersBySpending(int limit) {
        return customerRepository.getTopCustomersBySpending().stream()
            .limit(limit)
            .collect(java.util.stream.Collectors.toList());
    }
    
    public List<Customer> getTopCustomersByLoyalty(int limit) {
        return customerRepository.getTopCustomersByLoyalty().stream()
            .limit(limit)
            .collect(java.util.stream.Collectors.toList());
    }
    
    public long getNewCustomersSince(LocalDateTime since) {
        return customerRepository.countNewCustomersSince(since);
    }
}
