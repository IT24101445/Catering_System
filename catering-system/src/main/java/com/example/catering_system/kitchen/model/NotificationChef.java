package com.example.catering_system.kitchen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
public class NotificationChef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "Message", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String message;

    @Column(name = "Type", nullable = false, length = 50)
    private String type; // "menu_change", "schedule_update", "event_update", "system"

    @Column(name = "RelatedId")
    private Integer relatedId; // ID of related menu, schedule, or event

    @Column(name = "RelatedType", length = 50)
    private String relatedType; // "menu", "schedule", "event"

    @Column(name = "IsRead", nullable = false)
    private Boolean isRead = false;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "ReadAt")
    private LocalDateTime readAt;

    public NotificationChef() {
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public NotificationChef(String title, String message, String type, Integer relatedId, String relatedType) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedId = relatedId;
        this.relatedType = relatedType;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    // Convenience methods
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }

    public boolean isMenuChangeNotification() {
        return "menu_change".equalsIgnoreCase(this.type);
    }

    public boolean isScheduleUpdateNotification() {
        return "schedule_update".equalsIgnoreCase(this.type);
    }

    public boolean isEventUpdateNotification() {
        return "event_update".equalsIgnoreCase(this.type);
    }
}
