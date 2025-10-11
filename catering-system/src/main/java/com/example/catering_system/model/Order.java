package com.example.catering_system.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    private Date eventDate;
    private String eventType;
    private String location;
    private Integer guestCount;
    private String dietaryRequirements;

    private String selectedMenu; // comma-separated names or ids
    private String cuisines; // comma-separated

    private Double perPlatePrice;
    private Double totalAmount;

    private String status; // Pending, In Preparation, Ready, Completed

    private Boolean paymentConfirmed = false;

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
}


