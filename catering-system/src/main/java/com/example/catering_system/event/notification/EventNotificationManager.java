package com.example.catering_system.event.notification;

import com.example.catering_system.event.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Event Notification Manager
 * Handles all event-related notifications without circular dependencies
 */
@Component
public class EventNotificationManager {
    
    @Autowired
    private EventNotificationService notificationService;
    
    /**
     * Create notification when event is created
     */
    public void notifyEventCreated(Event event) {
        notificationService.createNotification(
            "New Event Created",
            "A new event '" + event.getName() + "' has been created for " + 
            event.getGuestCount() + " guests at " + event.getVenue(),
            "EVENT_CREATED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "ALL",
            event.getId()
        );
        
        // Create kitchen-specific notification
        notificationService.createNotification(
            "New Event for Kitchen",
            "New event '" + event.getName() + "' requires kitchen preparation for " + 
            event.getGuestCount() + " guests. Event date: " + event.getDateTime(),
            "EVENT_CREATED",
            "HIGH",
            "SYSTEM",
            "System",
            "KITCHEN",
            event.getId()
        );
        
        // Create delivery-specific notification
        notificationService.createNotification(
            "New Event for Delivery",
            "New event '" + event.getName() + "' at " + event.getVenue() + 
            " requires delivery service for " + event.getGuestCount() + " guests.",
            "EVENT_CREATED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "DELIVERY",
            event.getId()
        );
        
        System.out.println("Notifications created for new event: " + event.getName());
    }
    
    /**
     * Create notification when event status changes
     */
    public void notifyEventStatusChanged(Event event, String oldStatus, String newStatus) {
        String priority = "PLANNED".equals(newStatus) ? "HIGH" : "MEDIUM";
        
        notificationService.createNotification(
            "Event Status Changed",
            "Event '" + event.getName() + "' status changed from " + 
            oldStatus + " to " + newStatus,
            "STATUS_CHANGE",
            priority,
            "SYSTEM",
            "System",
            "ALL",
            event.getId()
        );
        
        // Kitchen-specific status notification
        notificationService.createNotification(
            "Event Status Update - Kitchen",
            "Event '" + event.getName() + "' status changed to " + newStatus + 
            ". Kitchen preparation required.",
            "STATUS_CHANGE",
            "PLANNED".equals(newStatus) ? "URGENT" : "MEDIUM",
            "SYSTEM",
            "System",
            "KITCHEN",
            event.getId()
        );
        
        // Delivery-specific status notification
        notificationService.createNotification(
            "Event Status Update - Delivery",
            "Event '" + event.getName() + "' status changed to " + newStatus + 
            ". Delivery planning required.",
            "STATUS_CHANGE",
            "PLANNED".equals(newStatus) ? "HIGH" : "MEDIUM",
            "SYSTEM",
            "System",
            "DELIVERY",
            event.getId()
        );
        
        System.out.println("Notifications created for status change: " + event.getName() + 
                         " (" + oldStatus + " -> " + newStatus + ")");
    }
    
    /**
     * Create notification when event is updated
     */
    public void notifyEventUpdated(Event event) {
        notificationService.createNotification(
            "Event Updated",
            "Event '" + event.getName() + "' has been updated",
            "EVENT_UPDATED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "ALL",
            event.getId()
        );
        
        // Kitchen-specific update notification
        notificationService.createNotification(
            "Event Details Updated - Kitchen",
            "Event '" + event.getName() + "' details have been updated. Please review changes.",
            "EVENT_UPDATED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "KITCHEN",
            event.getId()
        );
        
        // Delivery-specific update notification
        notificationService.createNotification(
            "Event Details Updated - Delivery",
            "Event '" + event.getName() + "' details have been updated. Please review delivery requirements.",
            "EVENT_UPDATED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "DELIVERY",
            event.getId()
        );
        
        System.out.println("Notifications created for event update: " + event.getName());
    }
    
    /**
     * Create notification when event is completed
     */
    public void notifyEventCompleted(Event event) {
        notificationService.createNotification(
            "Event Completed",
            "Event '" + event.getName() + "' has been completed successfully",
            "EVENT_COMPLETED",
            "HIGH",
            "SYSTEM",
            "System",
            "ALL",
            event.getId()
        );
        
        // Kitchen completion notification
        notificationService.createNotification(
            "Event Completed - Kitchen",
            "Event '" + event.getName() + "' has been completed successfully. Great job!",
            "EVENT_COMPLETED",
            "LOW",
            "SYSTEM",
            "System",
            "KITCHEN",
            event.getId()
        );
        
        // Delivery completion notification
        notificationService.createNotification(
            "Event Completed - Delivery",
            "Event '" + event.getName() + "' has been completed successfully. Delivery completed!",
            "EVENT_COMPLETED",
            "LOW",
            "SYSTEM",
            "System",
            "DELIVERY",
            event.getId()
        );
        
        System.out.println("Notifications created for event completion: " + event.getName());
    }
    
    /**
     * Create notification when event is deleted
     */
    public void notifyEventDeleted(Long eventId) {
        notificationService.createNotification(
            "Event Deleted",
            "Event with ID " + eventId + " has been deleted",
            "EVENT_DELETED",
            "HIGH",
            "SYSTEM",
            "System",
            "ALL",
            eventId
        );
        
        // Kitchen deletion notification
        notificationService.createNotification(
            "Event Cancelled - Kitchen",
            "Event with ID " + eventId + " has been cancelled. No preparation needed.",
            "EVENT_DELETED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "KITCHEN",
            eventId
        );
        
        // Delivery deletion notification
        notificationService.createNotification(
            "Event Cancelled - Delivery",
            "Event with ID " + eventId + " has been cancelled. No delivery needed.",
            "EVENT_DELETED",
            "MEDIUM",
            "SYSTEM",
            "System",
            "DELIVERY",
            eventId
        );
        
        System.out.println("Notifications created for event deletion: " + eventId);
    }
}
