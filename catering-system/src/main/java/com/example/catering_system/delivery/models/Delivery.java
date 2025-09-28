package com.example.catering_system.delivery.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;
    private Long bookingId;
    private Long driverId;
    private String deliveryAddress;
    private LocalDateTime scheduledTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private String status = "PENDING";
    private String notes;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Delivery() {}
    public Delivery(Long bookingId, String deliveryAddress, LocalDateTime scheduledTime) {
        this.bookingId = bookingId;
        this.deliveryAddress = deliveryAddress;
        this.scheduledTime = scheduledTime;
    }

    // Getters & Setters
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String address) { this.deliveryAddress = address; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime time) { this.scheduledTime = time; }
    public LocalDateTime getActualStartTime() { return actualStartTime; }
    public void setActualStartTime(LocalDateTime time) { this.actualStartTime = time; }
    public LocalDateTime getActualEndTime() { return actualEndTime; }
    public void setActualEndTime(LocalDateTime time) { this.actualEndTime = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (!List.of("PENDING", "ASSIGNED", "IN_TRANSIT", "COMPLETED", "FAILED").contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; this.updatedAt = LocalDateTime.now(); }

    public void startDelivery() {
        if ("ASSIGNED".equals(this.status)) {
            this.actualStartTime = LocalDateTime.now();
            this.status = "IN_TRANSIT";
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void completeDelivery() {
        if ("IN_TRANSIT".equals(this.status)) {
            this.actualEndTime = LocalDateTime.now();
            this.status = "COMPLETED";
            this.updatedAt = LocalDateTime.now();
        }
    }
}