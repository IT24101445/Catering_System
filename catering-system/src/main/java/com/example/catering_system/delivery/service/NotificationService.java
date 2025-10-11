package com.example.catering_system.delivery.service;

import com.example.catering_system.delivery.models.*;
import com.example.catering_system.delivery.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification createNotification(String message, String type, Driver driver, DeliveryAssignment assignment) {
        Notification notification = new Notification(message, type, driver, assignment);
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<Notification> getDriverNotifications(Long driverId) {
        return notificationRepository.findByDriverIdOrderByCreatedAtDesc(driverId);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadDriverNotifications(Long driverId) {
        return notificationRepository.findByDriverIdAndIsReadFalseOrderByCreatedAtDesc(driverId);
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long driverId) {
        return notificationRepository.countByDriverIdAndIsReadFalse(driverId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllAsRead(Long driverId) {
        List<Notification> unreadNotifications = getUnreadDriverNotifications(driverId);
        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    // Create assignment notification
    @Transactional
    public Notification notifyAssignmentCreated(DeliveryAssignment assignment) {
        try {
            String message = String.format("New delivery assignment: Order for %s", 
                assignment.getOrder().getCustomerName());
            return createNotification(message, "ASSIGNMENT", assignment.getDriver(), assignment);
        } catch (Exception e) {
            System.err.println("Failed to create assignment notification: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Create completion notification
    @Transactional
    public Notification notifyAssignmentCompleted(DeliveryAssignment assignment) {
        String message = String.format("Delivery completed: Order for %s", 
            assignment.getOrder().getCustomerName());
        return createNotification(message, "COMPLETION", assignment.getDriver(), assignment);
    }
}
