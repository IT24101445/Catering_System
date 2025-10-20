package com.example.catering_system.kitchen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    
    @Column(name = "EventId", nullable = false)
    private int eventId;
    
    @Column(name = "ChefId", nullable = false)
    private int chefId;
    
    @Column(name = "[Plan]", columnDefinition = "NVARCHAR(MAX)")
    private String plan;
    
    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    public Schedule() {
        this.plan = "";
        this.status = "draft";
    }

    public Schedule(int id, int eventId, int chefId, String plan, String status) {
        this.id = id;
        this.eventId = eventId;
        this.chefId = chefId;
        this.plan = plan;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convenience
    public boolean isDraft() { return "draft".equalsIgnoreCase(status); }
    public boolean isConfirmed() { return "confirmed".equalsIgnoreCase(status); }
    public boolean isArchived() { return "archived".equalsIgnoreCase(status); }
}
