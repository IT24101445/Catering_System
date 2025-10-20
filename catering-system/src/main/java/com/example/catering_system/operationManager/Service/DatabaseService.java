package com.example.catering_system.operationManager.Service;

import com.example.catering_system.operationManager.Entity.AssignedOrder;
import com.example.catering_system.operationManager.Entity.Manager;
import com.example.catering_system.operationManager.Entity.Order;
import com.example.catering_system.operationManager.Entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private DataSource dataSource;

    // Use Spring's DataSource for connection
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Validate manager login
    public Manager validateManager(String username, String password) {
        System.out.println("Attempting to validate manager: " + username);
        String sql = "SELECT * FROM managers WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            System.out.println("Database connection successful");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Manager found: " + rs.getString("username"));
                Manager manager = new Manager();
                manager.setId(rs.getInt("id"));
                manager.setUsername(rs.getString("username"));
                manager.setPassword(rs.getString("password"));
                return manager;
            } else {
                System.out.println("No manager found with username: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get pending orders
    public List<Order> getPendingOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM operation_orders WHERE status = 'PENDING'";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setCustomerName(rs.getString("customerName"));
                order.setDetails(rs.getString("details"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Get available staff
    public List<Staff> getAvailableStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE available = 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setId(rs.getInt("id"));
                staff.setName(rs.getString("name"));
                staff.setRole(rs.getString("role"));
                staff.setAvailable(rs.getBoolean("available"));
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    // Assign order to staff
    public boolean assignOrder(int orderId, int staffId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // Update order status
            String updateOrder = "UPDATE operation_orders SET status = 'ASSIGNED' WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateOrder);
            updateStmt.setInt(1, orderId);
            updateStmt.executeUpdate();

            // Create booking
            String createBooking = "INSERT INTO bookings (orderId, staffId, bookingDate, confirmed) VALUES (?, ?, GETDATE(), 1)";
            PreparedStatement bookingStmt = conn.prepareStatement(createBooking);
            bookingStmt.setInt(1, orderId);
            bookingStmt.setInt(2, staffId);
            bookingStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Send notification (simulated)
    public void sendNotification(String message) {
        System.out.println("Notification sent: " + message);
    }

    // Get assigned orders with staff details
    public List<AssignedOrder> getAssignedOrders() {
        List<AssignedOrder> assignedOrders = new ArrayList<>();
        String sql = "SELECT o.id as orderId, o.customerName, o.details, o.status, " +
                "s.id as staffId, s.name as staffName, s.role, b.bookingDate " +
                "FROM operation_orders o " +
                "JOIN bookings b ON o.id = b.orderId " +
                "JOIN staff s ON b.staffId = s.id " +
                "WHERE o.status = 'ASSIGNED'";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AssignedOrder assignedOrder = new AssignedOrder();
                assignedOrder.setOrderId(rs.getInt("orderId"));
                assignedOrder.setCustomerName(rs.getString("customerName"));
                assignedOrder.setDetails(rs.getString("details"));
                assignedOrder.setStatus(rs.getString("status"));
                assignedOrder.setStaffId(rs.getInt("staffId"));
                assignedOrder.setStaffName(rs.getString("staffName"));
                assignedOrder.setStaffRole(rs.getString("role"));
                assignedOrder.setBookingDate(rs.getString("bookingDate"));
                assignedOrders.add(assignedOrder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignedOrders;
    }

    // Create new order
    public boolean createOrder(String customerName, String details) {
        String sql = "INSERT INTO operation_orders (customerName, details, status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerName);
            stmt.setString(2, details);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete order
    public boolean deleteOrder(int id) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // First delete any bookings for this order
            String deleteBookings = "DELETE FROM bookings WHERE orderId = ?";
            PreparedStatement bookingStmt = conn.prepareStatement(deleteBookings);
            bookingStmt.setInt(1, id);
            bookingStmt.executeUpdate();

            // Then delete the order
            String deleteOrder = "DELETE FROM operation_orders WHERE id = ?";
            PreparedStatement orderStmt = conn.prepareStatement(deleteOrder);
            orderStmt.setInt(1, id);
            orderStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Create new staff
    public boolean createStaff(String name, String role) {
        String sql = "INSERT INTO staff (name, role, available) VALUES (?, ?, 1)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update staff availability
    public boolean updateStaffAvailability(int id, boolean available) {
        String sql = "UPDATE staff SET available = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, available);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete staff member
    public boolean deleteStaff(int id) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // First delete any bookings for this staff member
            String deleteBookings = "DELETE FROM bookings WHERE staffId = ?";
            PreparedStatement bookingStmt = conn.prepareStatement(deleteBookings);
            bookingStmt.setInt(1, id);
            bookingStmt.executeUpdate();

            // Then delete the staff member
            String deleteStaff = "DELETE FROM staff WHERE id = ?";
            PreparedStatement staffStmt = conn.prepareStatement(deleteStaff);
            staffStmt.setInt(1, id);
            staffStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Delete assigned order (with booking cleanup)
    public boolean deleteAssignedOrder(int orderId) {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // First delete any bookings for this order
            String deleteBookings = "DELETE FROM bookings WHERE orderId = ?";
            PreparedStatement bookingStmt = conn.prepareStatement(deleteBookings);
            bookingStmt.setInt(1, orderId);
            bookingStmt.executeUpdate();

            // Then delete the order
            String deleteOrder = "DELETE FROM operation_orders WHERE id = ?";
            PreparedStatement orderStmt = conn.prepareStatement(deleteOrder);
            orderStmt.setInt(1, orderId);
            orderStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}