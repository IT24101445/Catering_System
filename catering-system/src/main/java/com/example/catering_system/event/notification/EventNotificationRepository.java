package com.example.catering_system.event.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Event Notification operations
 */
@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotification, Long> {
    
    /**
     * Find notifications by event ID
     */
    List<EventNotification> findByEventIdOrderByCreatedAtDesc(Long eventId);
    
    /**
     * Find unread notifications by recipient type
     */
    List<EventNotification> findByRecipientTypeAndIsReadOrderByCreatedAtDesc(String recipientType, Boolean isRead);
    
    /**
     * Find notifications by sender type
     */
    List<EventNotification> findBySenderTypeOrderByCreatedAtDesc(String senderType);
    
    /**
     * Find notifications by priority
     */
    List<EventNotification> findByPriorityOrderByCreatedAtDesc(String priority);
    
    /**
     * Find notifications for specific staff member
     */
    @Query("SELECT n FROM EventNotification n WHERE n.recipientType = :recipientType AND (n.recipientName = :staffName OR n.recipientName IS NULL) ORDER BY n.createdAt DESC")
    List<EventNotification> findNotificationsForStaff(@Param("recipientType") String recipientType, @Param("staffName") String staffName);
    
    /**
     * Count unread notifications by recipient type
     */
    Long countByRecipientTypeAndIsRead(String recipientType, Boolean isRead);
    
    /**
     * Find urgent notifications
     */
    List<EventNotification> findByPriorityAndIsReadOrderByCreatedAtDesc(String priority, Boolean isRead);
}
