package com.example.catering_system.kitchen.dao;

import com.example.catering_system.kitchen.model.EventChef;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventChefDAO {

    private final Connection conn;

    public EventChefDAO(Connection conn) {
        this.conn = conn;
    }

    // Create: Insert new event
    public int insertEvent(String name, Integer guestCount, LocalDateTime deliveryTime, 
                          String location, String status, String notes) throws SQLException {
        final String sql = "INSERT INTO Events (Name, GuestCount, DeliveryTime, Location, Status, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, safeText(name));
            ps.setInt(2, guestCount != null ? guestCount : 0);
            ps.setTimestamp(3, deliveryTime != null ? Timestamp.valueOf(deliveryTime) : null);
            ps.setString(4, safeText(location));
            ps.setString(5, safeStatus(status));
            ps.setString(6, safeText(notes));
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        
        // Fallback
        try (PreparedStatement ps2 = conn.prepareStatement("SELECT MAX(Id) FROM Events");
             ResultSet rs2 = ps2.executeQuery()) {
            return rs2.next() ? rs2.getInt(1) : 0;
        }
    }

    // Read: Get all events (newest first)
    public List<EventChef> getAllEvents() throws SQLException {
        final String sql = "SELECT Id, Name, GuestCount, DeliveryTime, Location, Status, Notes FROM Events ORDER BY Id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<EventChef> events = new ArrayList<>();
            while (rs.next()) events.add(mapRow(rs));
            return events;
        }
    }

    // Read: Get events by status
    public List<EventChef> getEventsByStatus(String status) throws SQLException {
        try {
            final String sql = "SELECT Id, Name, GuestCount, DeliveryTime, Location, Status, Notes FROM Events WHERE LOWER(Status) = LOWER(?) ORDER BY Id DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, safeStatus(status));
                try (ResultSet rs = ps.executeQuery()) {
                    List<EventChef> events = new ArrayList<>();
                    while (rs.next()) events.add(mapRow(rs));
                    return events;
                }
            }
        } catch (SQLException e) {
            // If Events table doesn't exist, return empty list
            System.out.println("EventChefDAO: Events table not found, returning empty list");
            return new ArrayList<>();
        }
    }

    // Read: Get confirmed events
    public List<EventChef> getConfirmedEvents() throws SQLException {
        return getEventsByStatus("confirmed");
    }

    // Read: Get event by ID
    public EventChef getEventById(int id) throws SQLException {
        try {
            final String sql = "SELECT Id, Name, GuestCount, DeliveryTime, Location, Status, Notes FROM Events WHERE Id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapRow(rs) : null;
                }
            }
        } catch (SQLException e) {
            // If Events table doesn't exist, return null
            System.out.println("EventChefDAO: Events table not found, returning null");
            return null;
        }
    }

    // Read: Get events with upcoming delivery times
    public List<EventChef> getUpcomingEvents() throws SQLException {
        try {
            // First try with DeliveryTime column
            final String sql = "SELECT Id, Name, GuestCount, DeliveryTime, Location, Status, Notes FROM Events WHERE DeliveryTime > GETDATE() ORDER BY DeliveryTime ASC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                List<EventChef> events = new ArrayList<>();
                while (rs.next()) events.add(mapRow(rs));
                return events;
            }
        } catch (SQLException e) {
            // If DeliveryTime column doesn't exist, try without it
            try {
                System.out.println("EventChefDAO: DeliveryTime column not found, trying without it");
                final String sql = "SELECT Id, Name, GuestCount, Location, Status, Notes FROM Events ORDER BY Id ASC";
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    List<EventChef> events = new ArrayList<>();
                    while (rs.next()) events.add(mapRowWithoutDeliveryTime(rs));
                    return events;
                }
            } catch (SQLException e2) {
                // If Events table doesn't exist at all, return empty list
                System.out.println("EventChefDAO: Events table not found, returning empty list");
                return new ArrayList<>();
            }
        }
    }

    // Update: Update event
    public boolean updateEvent(int id, String name, Integer guestCount, LocalDateTime deliveryTime, 
                              String location, String status, String notes) throws SQLException {
        final String sql = "UPDATE Events SET Name = ?, GuestCount = ?, DeliveryTime = ?, Location = ?, Status = ?, Notes = ? WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, safeText(name));
            ps.setInt(2, guestCount != null ? guestCount : 0);
            ps.setTimestamp(3, deliveryTime != null ? Timestamp.valueOf(deliveryTime) : null);
            ps.setString(4, safeText(location));
            ps.setString(5, safeStatus(status));
            ps.setString(6, safeText(notes));
            ps.setInt(7, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Delete: Remove event
    public boolean deleteEvent(int id) throws SQLException {
        final String sql = "DELETE FROM Events WHERE Id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // Count all events
    public int countAll() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Events");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Mapper
    private EventChef mapRow(ResultSet rs) throws SQLException {
        EventChef event = new EventChef();
        event.setId(rs.getInt("Id"));
        event.setName(rs.getString("Name"));
        event.setGuestCount(rs.getInt("GuestCount"));
        
        Timestamp deliveryTimestamp = rs.getTimestamp("DeliveryTime");
        if (deliveryTimestamp != null) {
            event.setDeliveryTime(deliveryTimestamp.toLocalDateTime());
        }
        
        event.setLocation(rs.getString("Location"));
        event.setStatus(rs.getString("Status"));
        event.setNotes(rs.getString("Notes"));
        return event;
    }

    // Mapper without DeliveryTime column
    private EventChef mapRowWithoutDeliveryTime(ResultSet rs) throws SQLException {
        EventChef event = new EventChef();
        event.setId(rs.getInt("Id"));
        event.setName(rs.getString("Name"));
        event.setGuestCount(rs.getInt("GuestCount"));
        // No DeliveryTime column, set to null
        event.setDeliveryTime(null);
        event.setLocation(rs.getString("Location"));
        event.setStatus(rs.getString("Status"));
        event.setNotes(rs.getString("Notes"));
        return event;
    }

    private String safeText(String v) {
        return v == null ? "" : v;
    }

    private String safeStatus(String v) {
        return (v == null || v.trim().isEmpty()) ? "planned" : v.trim();
    }
}
