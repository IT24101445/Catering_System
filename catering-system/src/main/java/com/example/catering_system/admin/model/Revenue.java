package com.example.catering_system.admin.model;

import jakarta.persistence.*;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Table(name = "revenues")
public class Revenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source; // "CATERING_ORDER", "EVENT", "PACKAGE", "SUBSCRIPTION"
    private String description;
    private double amount;
    
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date revenueDate;
    
    private String customerName;
    private String eventName;
    private String status; // "RECEIVED", "PENDING", "OVERDUE"

    // Constructors
    public Revenue() {}

    public Revenue(String source, String description, double amount, Date revenueDate, String customerName) {
        this.source = source;
        this.description = description;
        this.amount = amount;
        this.revenueDate = revenueDate;
        this.customerName = customerName;
        this.status = "RECEIVED";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getRevenueDate() {
        return revenueDate;
    }

    public void setRevenueDate(Date revenueDate) {
        this.revenueDate = revenueDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
