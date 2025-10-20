package com.example.catering_system.customer.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.catering_system.customer.model.Order;

/**
 * Repository interface for Order entity
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByCustomer_Id(Long customerId);
    
    List<Order> findByOrderType(Order.OrderType orderType);
    
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
    
    @Query("SELECT o FROM CustomerDomainOrder o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM CustomerDomainOrder o WHERE o.status = :status AND o.orderDate >= :since")
    List<Order> findRecentOrdersByStatus(@Param("status") Order.OrderStatus status, 
                                        @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(o) FROM CustomerDomainOrder o WHERE o.status = :status")
    long countByStatus(@Param("status") Order.OrderStatus status);
    
    @Query("SELECT SUM(o.totalAmount) FROM CustomerDomainOrder o WHERE o.status = 'DELIVERED' AND o.orderDate >= :since")
    Double getTotalRevenueSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT o FROM CustomerDomainOrder o WHERE o.assignedChef = :chefName AND o.status IN ('PREPARING', 'READY')")
    List<Order> findActiveOrdersByChef(@Param("chefName") String chefName);
    
    // Analytics queries
    @Query("SELECT COUNT(o) FROM CustomerDomainOrder o WHERE o.orderDate >= :since")
    long countOrdersSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT SUM(o.totalAmount) FROM CustomerDomainOrder o WHERE o.orderDate >= :since")
    Double getTotalRevenueAllSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT AVG(o.totalAmount) FROM CustomerDomainOrder o WHERE o.status = 'DELIVERED' AND o.orderDate >= :since")
    Double getAverageOrderValueSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT o.orderType, COUNT(o) FROM CustomerDomainOrder o WHERE o.orderDate >= :since GROUP BY o.orderType")
    List<Object[]> getOrderTypeStatsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT o.status, COUNT(o) FROM CustomerDomainOrder o WHERE o.orderDate >= :since GROUP BY o.status")
    List<Object[]> getOrderStatusStatsSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT o FROM CustomerDomainOrder o WHERE o.orderDate >= :since ORDER BY o.totalAmount DESC")
    List<Order> getTopOrdersByValueSince(@Param("since") LocalDateTime since);
}
