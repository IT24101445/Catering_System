package com.example.catering_system.customer.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Order Status History Entity for tracking order status changes
 */
@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private Order.OrderStatus fromStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private Order.OrderStatus toStatus;
    
    @Column(name = "changed_by")
    private String changedBy;
    
    @Column(name = "change_reason", length = 500)
    private String changeReason;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt = LocalDateTime.now();
    
    // Constructors
    public OrderStatusHistory() {}
    
    public OrderStatusHistory(Order order, Order.OrderStatus fromStatus, 
                             Order.OrderStatus toStatus, String changedBy) {
        this.order = order;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedBy = changedBy;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public Order.OrderStatus getFromStatus() { return fromStatus; }
    public void setFromStatus(Order.OrderStatus fromStatus) { this.fromStatus = fromStatus; }
    
    public Order.OrderStatus getToStatus() { return toStatus; }
    public void setToStatus(Order.OrderStatus toStatus) { this.toStatus = toStatus; }
    
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    
    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
