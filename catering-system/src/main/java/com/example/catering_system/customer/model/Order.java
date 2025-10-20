package com.example.catering_system.customer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Enhanced Order Entity with Advanced Order Management Features
 */
@Entity(name = "CustomerDomainOrder")
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer is required")
    @JsonIgnore
    private Customer customer;
    
    // Denormalized customer name for reporting; required by DB schema
    @Column(name = "customer_name")
    private String customerName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType = OrderType.DELIVERY;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    // Order Details
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();
    
    @Column(name = "requested_delivery_date")
    private LocalDateTime requestedDeliveryDate;
    
    @Column(name = "confirmed_delivery_date")
    private LocalDateTime confirmedDeliveryDate;
    
    @Column(name = "actual_delivery_date")
    private LocalDateTime actualDeliveryDate;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;
    
    // Financial Information
    @Column(name = "subtotal", precision = 12, scale = 2, nullable = false)
    @Positive(message = "Subtotal must be positive")
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Column(name = "delivery_fee", precision = 8, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;
    
    @Column(name = "service_fee", precision = 8, scale = 2)
    private BigDecimal serviceFee = BigDecimal.ZERO;
    
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(name = "tip_amount", precision = 8, scale = 2)
    private BigDecimal tipAmount = BigDecimal.ZERO;
    
    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    // Delivery Information
    @Column(name = "delivery_address", length = 1000)
    private String deliveryAddress;
    
    @Column(name = "dropoff_address", length = 1000, nullable = false)
    private String dropoffAddress;
    
    @Column(name = "pickup_address", length = 1000, nullable = false)
    private String pickupAddress;
    
    @Column(name = "delivery_instructions", length = 2000)
    private String deliveryInstructions;
    
    @Column(name = "delivery_contact_name")
    private String deliveryContactName;
    
    @Column(name = "delivery_contact_phone")
    private String deliveryContactPhone;
    
    // Event Information (for catering orders)
    @Column(name = "event_name")
    private String eventName;
    
    @Column(name = "event_type")
    private String eventType; // WEDDING, CORPORATE, BIRTHDAY, etc.
    
    @Column(name = "guest_count")
    private Integer guestCount;
    
    @Column(name = "event_start_time")
    private LocalDateTime eventStartTime;
    
    @Column(name = "event_end_time")
    private LocalDateTime eventEndTime;
    
    // Additional Information
    @Column(name = "special_instructions", length = 2000)
    private String specialInstructions;
    
    @Column(name = "dietary_requirements", length = 1000)
    private String dietaryRequirements;
    
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
    
    @Column(name = "internal_notes", length = 2000)
    private String internalNotes;
    
    // Tracking
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "assigned_chef")
    private String assignedChef;
    
    @Column(name = "assigned_delivery_person")
    private String assignedDeliveryPerson;
    
    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();
    
    // Enums
    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, READY, OUT_FOR_DELIVERY, 
        DELIVERED, COMPLETED, CANCELLED, REFUNDED
    }
    
    public enum OrderType {
        DELIVERY, PICKUP, DINE_IN, CATERING
    }
    
    public enum PaymentStatus {
        PENDING, PAID, PARTIALLY_PAID, REFUNDED, FAILED
    }
    
    // Constructors
    public Order() {
        this.orderNumber = generateOrderNumber();
    }
    
    public Order(Customer customer, OrderType orderType) {
        this();
        this.customer = customer;
        this.orderType = orderType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    // Helper method to get customer ID
    public Long getCustomerId() { 
        return customer != null ? customer.getId() : null; 
    }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    
    public OrderType getOrderType() { return orderType; }
    public void setOrderType(OrderType orderType) { this.orderType = orderType; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getRequestedDeliveryDate() { return requestedDeliveryDate; }
    public void setRequestedDeliveryDate(LocalDateTime requestedDeliveryDate) { this.requestedDeliveryDate = requestedDeliveryDate; }
    
    public LocalDateTime getConfirmedDeliveryDate() { return confirmedDeliveryDate; }
    public void setConfirmedDeliveryDate(LocalDateTime confirmedDeliveryDate) { this.confirmedDeliveryDate = confirmedDeliveryDate; }
    
    public LocalDateTime getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(LocalDateTime actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }
    
    public Integer getPreparationTimeMinutes() { return preparationTimeMinutes; }
    public void setPreparationTimeMinutes(Integer preparationTimeMinutes) { this.preparationTimeMinutes = preparationTimeMinutes; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    
    public BigDecimal getTaxAmount() { return taxAmount; }
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    
    public BigDecimal getServiceFee() { return serviceFee; }
    public void setServiceFee(BigDecimal serviceFee) { this.serviceFee = serviceFee; }
    
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    
    public BigDecimal getTipAmount() { return tipAmount; }
    public void setTipAmount(BigDecimal tipAmount) { this.tipAmount = tipAmount; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    
    public String getDropoffAddress() { return dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }
    
    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    
    public String getDeliveryContactName() { return deliveryContactName; }
    public void setDeliveryContactName(String deliveryContactName) { this.deliveryContactName = deliveryContactName; }
    
    public String getDeliveryContactPhone() { return deliveryContactPhone; }
    public void setDeliveryContactPhone(String deliveryContactPhone) { this.deliveryContactPhone = deliveryContactPhone; }
    
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    
    public LocalDateTime getEventStartTime() { return eventStartTime; }
    public void setEventStartTime(LocalDateTime eventStartTime) { this.eventStartTime = eventStartTime; }
    
    public LocalDateTime getEventEndTime() { return eventEndTime; }
    public void setEventEndTime(LocalDateTime eventEndTime) { this.eventEndTime = eventEndTime; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    
    public String getDietaryRequirements() { return dietaryRequirements; }
    public void setDietaryRequirements(String dietaryRequirements) { this.dietaryRequirements = dietaryRequirements; }
    
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    
    public String getInternalNotes() { return internalNotes; }
    public void setInternalNotes(String internalNotes) { this.internalNotes = internalNotes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getAssignedChef() { return assignedChef; }
    public void setAssignedChef(String assignedChef) { this.assignedChef = assignedChef; }
    
    public String getAssignedDeliveryPerson() { return assignedDeliveryPerson; }
    public void setAssignedDeliveryPerson(String assignedDeliveryPerson) { this.assignedDeliveryPerson = assignedDeliveryPerson; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    public List<OrderStatusHistory> getStatusHistory() { return statusHistory; }
    public void setStatusHistory(List<OrderStatusHistory> statusHistory) { this.statusHistory = statusHistory; }
    
    // Utility methods
    public void calculateTotalAmount() {
        this.totalAmount = subtotal
            .add(taxAmount)
            .add(deliveryFee)
            .add(serviceFee)
            .add(tipAmount)
            .subtract(discountAmount);
    }
    
    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrder(this);
    }
    
    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setOrder(null);
    }
    
    private String generateOrderNumber() {
        // Generate order number with format: ORD-YYYYMMDD-XXXX
        LocalDateTime now = LocalDateTime.now();
        String datePrefix = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timeSuffix = String.format("%04d", now.getHour() * 100 + now.getMinute());
        return "ORD-" + datePrefix + "-" + timeSuffix;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
