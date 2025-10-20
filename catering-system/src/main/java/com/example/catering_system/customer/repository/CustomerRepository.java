package com.example.catering_system.customer.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.catering_system.customer.model.Customer;

/**
 * Repository interface for Customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByStatus(Customer.CustomerStatus status);
    
    List<Customer> findByCustomerType(Customer.CustomerType customerType);
    
    @Query("SELECT c FROM Customer c WHERE c.totalSpent >= :minAmount")
    List<Customer> findHighValueCustomers(@Param("minAmount") BigDecimal minAmount);
    
    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :minPoints")
    List<Customer> findLoyalCustomers(@Param("minPoints") BigDecimal minPoints);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.status = :status")
    long countByStatus(@Param("status") Customer.CustomerStatus status);
    
    @Query("SELECT SUM(c.totalSpent) FROM Customer c WHERE c.status = 'ACTIVE'")
    BigDecimal getTotalRevenueFromActiveCustomers();
    
    // Analytics queries
    @Query("SELECT c.customerType, COUNT(c) FROM Customer c GROUP BY c.customerType")
    List<Object[]> getCustomerTypeStats();
    
    @Query("SELECT c.status, COUNT(c) FROM Customer c GROUP BY c.status")
    List<Object[]> getCustomerStatusStats();
    
    @Query("SELECT c FROM Customer c ORDER BY c.totalSpent DESC")
    List<Customer> getTopCustomersBySpending();
    
    @Query("SELECT c FROM Customer c ORDER BY c.loyaltyPoints DESC")
    List<Customer> getTopCustomersByLoyalty();
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.registrationDate >= :since")
    long countNewCustomersSince(@Param("since") java.time.LocalDateTime since);
    
    boolean existsByEmail(String email);
}
