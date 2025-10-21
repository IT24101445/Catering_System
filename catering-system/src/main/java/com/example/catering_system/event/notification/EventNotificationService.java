package com.example.catering_system.event.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing event notifications
 */
@Service
public class EventNotificationService {
    
    @Autowired
    private EventNotificationRepository notificationRepository;
    
    /**
     * Create a new notification
     */
    public EventNotification createNotification(String title, String message, String notificationType,
                                               String priority, String senderType, String senderName,
                                               String recipientType, Long eventId) {
        EventNotification notification = new EventNotification(title, message, notificationType,
                                                           priority, senderType, senderName,
                                                           recipientType, eventId);
        return notificationRepository.save(notification);
    }
    
    /**
     * Create admin message to staff
     */
    public EventNotification createAdminMessage(String message, String recipientType, 
                                               String recipientName, Long eventId, String priority) {
        return createNotification(
            "Admin Message",
            message,
            "ADMIN_MESSAGE",
            priority != null ? priority : "MEDIUM",
            "ADMIN",
            "System Admin",
            recipientType,
            eventId
        );
    }
    
    /**
     * Create staff response to admin
     */
    public EventNotification createStaffResponse(String message, String senderName, 
                                               String senderType, Long eventId) {
        return createNotification(
            "Staff Response",
            message,
            "STAFF_RESPONSE",
            "MEDIUM",
            senderType,
            senderName,
            "ADMIN",
            eventId
        );
    }
    
    /**
     * Get all notifications for an event
     */
    public List<EventNotification> getNotificationsByEvent(Long eventId) {
        return notificationRepository.findByEventIdOrderByCreatedAtDesc(eventId);
    }
    
    /**
     * Get unread notifications for a recipient type
     */
    public List<EventNotification> getUnreadNotifications(String recipientType) {
        return notificationRepository.findByRecipientTypeAndIsReadOrderByCreatedAtDesc(recipientType, false);
    }
    
    /**
     * Get notifications for specific staff member
     */
    public List<EventNotification> getNotificationsForStaff(String recipientType, String staffName) {
        return notificationRepository.findNotificationsForStaff(recipientType, staffName);
    }
    
    /**
     * Mark notification as read
     */
    public void markAsRead(Long notificationId) {
        Optional<EventNotification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            EventNotification notif = notification.get();
            notif.setIsRead(true);
            notif.setReadAt(LocalDateTime.now());
            notificationRepository.save(notif);
        }
    }
    
    /**
     * Add response to a notification
     */
    public void addResponse(Long notificationId, String response) {
        Optional<EventNotification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            EventNotification notif = notification.get();
            notif.setResponse(response);
            notif.setRespondedAt(LocalDateTime.now());
            notificationRepository.save(notif);
        }
    }
    
    /**
     * Get urgent notifications
     */
    public List<EventNotification> getUrgentNotifications() {
        return notificationRepository.findByPriorityAndIsReadOrderByCreatedAtDesc("URGENT", false);
    }
    
    /**
     * Count unread notifications for recipient type
     */
    public Long countUnreadNotifications(String recipientType) {
        return notificationRepository.countByRecipientTypeAndIsRead(recipientType, false);
    }
    
    /**
     * Get all notifications
     */
    public List<EventNotification> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
