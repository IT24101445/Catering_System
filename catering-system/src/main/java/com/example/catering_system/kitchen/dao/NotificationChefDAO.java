package com.example.catering_system.kitchen.dao;

import com.example.catering_system.kitchen.model.NotificationChef;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationChefDAO {

    private final Connection conn;

    public NotificationChefDAO(Connection conn) {
        this.conn = conn;
    }

    // Create: Insert new notification
    public int insertNotification(String title, String message, String type, Integer relatedId, String relatedType) throws SQLException {
        final String sql = "INSERT INTO Notifications (Title, Message, Type, RelatedId, RelatedType, IsRead, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, safeText(title));
            ps.setString(2, safeText(message));
            ps.setString(3, safeText(type));
            ps.setObject(4, relatedId);
            ps.setString(5, safeText(relatedType));
            ps.setBoolean(6, false);
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        
        // Fallback
        try (PreparedStatement ps2 = conn.prepareStatement("SELECT MAX(Id) FROM Notifications");
             ResultSet rs2 = ps2.executeQuery()) {
            return rs2.next() ? rs2.getInt(1) : 0;
        }
    }

    // Read: Get all notifications (newest first)
    public List<NotificationChef> getAllNotifications() throws SQLException {
        final String sql = "SELECT Id, Title, Message, Type, RelatedId, RelatedType, IsRead, CreatedAt, ReadAt FROM Notifications ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<NotificationChef> notifications = new ArrayList<>();
            while (rs.next()) notifications.add(mapRow(rs));
            return notifications;
        }
    }

    // Read: Get unread notifications
    public List<NotificationChef> getUnreadNotifications() throws SQLException {
        final String sql = "SELECT Id, Title, Message, Type, RelatedId, RelatedType, IsRead, CreatedAt, ReadAt FROM Notifications WHERE IsRead = 0 ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<NotificationChef> notifications = new ArrayList<>();
            while (rs.next()) notifications.add(mapRow(rs));
            return notifications;
        }
    }

    // Read: Get notifications by type
    public List<NotificationChef> getNotificationsByType(String type) throws SQLException {
        final String sql = "SELECT Id, Title, Message, Type, RelatedId, RelatedType, IsRead, CreatedAt, ReadAt FROM Notifications WHERE Type = ? ORDER BY CreatedAt DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, safeText(type));
            try (ResultSet rs = ps.executeQuery()) {
                List<NotificationChef> notifications = new ArrayList<>();
                while (rs.next()) notifications.add(mapRow(rs));
                return notifications;
            }
        }
    }

    // Read: Get menu change notifications
    public List<NotificationChef> getMenuChangeNotifications() throws SQLException {
        return getNotificationsByType("menu_change");
    }

    // Read: Get notification by ID
    public NotificationChef getNotificationById(int id) throws SQLException {
        final String sql = "SELECT Id, Title, Message, Type, RelatedId, RelatedType, IsRead, CreatedAt, ReadAt FROM Notifications WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // Update: Mark notification as read
    public boolean markAsRead(int id) throws SQLException {
        final String sql = "UPDATE Notifications SET IsRead = 1, ReadAt = ? WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Update: Mark all notifications as read
    public boolean markAllAsRead() throws SQLException {
        final String sql = "UPDATE Notifications SET IsRead = 1, ReadAt = ? WHERE IsRead = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            return ps.executeUpdate() >= 0;
        }
    }

    // Delete: Remove notification
    public boolean deleteNotification(int id) throws SQLException {
        final String sql = "DELETE FROM Notifications WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Delete: Remove old notifications (older than specified days)
    public boolean deleteOldNotifications(int daysOld) throws SQLException {
        final String sql = "DELETE FROM Notifications WHERE CreatedAt < DATEADD(day, -?, GETDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, daysOld);
            return ps.executeUpdate() >= 0;
        }
    }

    // Count unread notifications
    public int countUnread() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Notifications WHERE IsRead = 0");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Mapper
    private NotificationChef mapRow(ResultSet rs) throws SQLException {
        NotificationChef notification = new NotificationChef();
        notification.setId(rs.getInt("Id"));
        notification.setTitle(rs.getString("Title"));
        notification.setMessage(rs.getString("Message"));
        notification.setType(rs.getString("Type"));
        
        Integer relatedId = rs.getInt("RelatedId");
        if (!rs.wasNull()) {
            notification.setRelatedId(relatedId);
        }
        
        notification.setRelatedType(rs.getString("RelatedType"));
        notification.setIsRead(rs.getBoolean("IsRead"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            notification.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp readAt = rs.getTimestamp("ReadAt");
        if (readAt != null) {
            notification.setReadAt(readAt.toLocalDateTime());
        }
        
        return notification;
    }

    private String safeText(String v) {
        return v == null ? "" : v;
    }
}
