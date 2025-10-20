package com.example.catering_system.kitchen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Events")
public class EventChef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name", nullable = false, length = 200)
    private String name;

    @Column(name = "GuestCount", nullable = false)
    private Integer guestCount;

    @Column(name = "DeliveryTime", nullable = false)
    private LocalDateTime deliveryTime;

    @Column(name = "Location", length = 300)
    private String location;

    @Column(name = "Status", nullable = false, length = 50)
    private String status; // "planned", "confirmed", "in_progress", "completed", "cancelled"

    @Column(name = "Notes", columnDefinition = "NVARCHAR(MAX)")
    private String notes;

    public EventChef() {
        this.guestCount = 0;
        this.status = "planned";
    }

    public EventChef(String name, Integer guestCount, LocalDateTime deliveryTime, String location, String status) {
        this.name = name;
        this.guestCount = guestCount;
        this.deliveryTime = deliveryTime;
        this.location = location;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Convenience methods
    public boolean isConfirmed() {
        return "confirmed".equalsIgnoreCase(this.status);
    }

    public boolean isPlanned() {
        return "planned".equalsIgnoreCase(this.status);
    }

    public boolean isInProgress() {
        return "in_progress".equalsIgnoreCase(this.status);
    }

    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(this.status);
    }

    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(this.status);
    }
}
