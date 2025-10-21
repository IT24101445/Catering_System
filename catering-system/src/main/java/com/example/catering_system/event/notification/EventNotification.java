package com.example.catering_system.event.notification;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Event Notification Entity
 * Stores notifications for event-related communications between admin and staff
 */
@Entity
@Table(name = "event_notifications")
public class EventNotification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(nullable = false)
    private String notificationType; // "EVENT_CREATED", "STATUS_CHANGE", "ADMIN_MESSAGE", "STAFF_RESPONSE"
    
    @Column(nullable = false)
    private String priority; // "LOW", "MEDIUM", "HIGH", "URGENT"
    
    @Column(nullable = false)
    private String senderType; // "ADMIN", "KITCHEN", "DELIVERY", "SYSTEM"
    
    @Column(nullable = false)
    private String senderName;
    
    @Column(nullable = false)
    private String recipientType; // "ALL", "KITCHEN", "DELIVERY", "ADMIN"
    
    @Column
    private String recipientName; // Specific staff member name (optional)
    
    @Column(nullable = false)
    private Long eventId;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime readAt;
    
    @Column
    private String response; // Staff response to admin message
    
    @Column
    private LocalDateTime respondedAt;
    
    // Constructors
    public EventNotification() {}
    
    public EventNotification(String title, String message, String notificationType, 
                           String priority, String senderType, String senderName,
                           String recipientType, Long eventId) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.priority = priority;
        this.senderType = senderType;
        this.senderName = senderName;
        this.recipientType = recipientType;
        this.eventId = eventId;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    
    public String getSenderType() { return senderType; }
    public void setSenderType(String senderType) { this.senderType = senderType; }
    
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    
    public String getRecipientType() { return recipientType; }
    public void setRecipientType(String recipientType) { this.recipientType = recipientType; }
    
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
}
