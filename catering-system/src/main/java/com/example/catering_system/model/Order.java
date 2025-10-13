package com.example.catering_system.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity(name = "CustomerOrder")
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "event_date")
    private Date eventDate;
    
    @Column(name = "event_type")
    private String eventType;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "guest_count")
    private Integer guestCount;
    
    @Column(name = "dietary_requirements")
    private String dietaryRequirements;

    @Column(name = "selected_menu")
    private String selectedMenu; // comma-separated names or ids
    
    @Column(name = "cuisines")
    private String cuisines; // comma-separated

    @Column(name = "per_plate_price")
    private Double perPlatePrice;
    
    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "status")
    private String status; // Pending, In Preparation, Ready, Completed

    @Column(name = "payment_confirmed")
    private Boolean paymentConfirmed = false;

    // Kitchen and scheduling enhancements
    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "delivery_time")
    private java.time.LocalDateTime deliveryTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    public String getDietaryRequirements() { return dietaryRequirements; }
    public void setDietaryRequirements(String dietaryRequirements) { this.dietaryRequirements = dietaryRequirements; }
    public String getSelectedMenu() { return selectedMenu; }
    public void setSelectedMenu(String selectedMenu) { this.selectedMenu = selectedMenu; }
    public String getCuisines() { return cuisines; }
    public void setCuisines(String cuisines) { this.cuisines = cuisines; }
    public Double getPerPlatePrice() { return perPlatePrice; }
    public void setPerPlatePrice(Double perPlatePrice) { this.perPlatePrice = perPlatePrice; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getPaymentConfirmed() { return paymentConfirmed; }
    public void setPaymentConfirmed(Boolean paymentConfirmed) { this.paymentConfirmed = paymentConfirmed; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public java.time.LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(java.time.LocalDateTime deliveryTime) { this.deliveryTime = deliveryTime; }
}


