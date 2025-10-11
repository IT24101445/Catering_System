package com.example.catering_system.delivery.controllers;

import com.example.catering_system.delivery.models.Notification;
import com.example.catering_system.delivery.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Notification>> getDriverNotifications(@PathVariable Long driverId) {
        List<Notification> notifications = notificationService.getDriverNotifications(driverId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/driver/{driverId}/unread")
    public ResponseEntity<List<Notification>> getUnreadDriverNotifications(@PathVariable Long driverId) {
        List<Notification> notifications = notificationService.getUnreadDriverNotifications(driverId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/driver/{driverId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long driverId) {
        Long count = notificationService.getUnreadCount(driverId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{notificationId}/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/driver/{driverId}/mark-all-read")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long driverId) {
        notificationService.markAllAsRead(driverId);
        return ResponseEntity.ok().build();
    }
}
