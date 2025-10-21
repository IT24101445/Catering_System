package com.example.catering_system.operationManager.Service;

import com.example.catering_system.operationManager.Entity.Manager;
import com.example.catering_system.operationManager.Entity.Order;
import com.example.catering_system.operationManager.Entity.Staff;
import com.example.catering_system.operationManager.Entity.AssignedOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("simpleOperationDatabaseService")
public class SimpleDatabaseService {
    
    // In-memory storage for testing
    private static final List<Order> orders = new ArrayList<>();
    private static final List<Staff> staffList = new ArrayList<>();
    private static final List<AssignedOrder> assignedOrders = new ArrayList<>();
    private static final AtomicInteger orderIdCounter = new AtomicInteger(1);
    private static final AtomicInteger staffIdCounter = new AtomicInteger(1);
    private static final AtomicInteger assignedOrderIdCounter = new AtomicInteger(1);
    
    // Initialize with sample data
    static {
        // Add sample orders
        Order order1 = new Order();
        order1.setId(1);
        order1.setCustomerName("ABC Corporation");
        order1.setDetails("Corporate lunch for 50 people");
        order1.setStatus("PENDING");
        orders.add(order1);
        
        Order order2 = new Order();
        order2.setId(2);
        order2.setCustomerName("XYZ Events");
        order2.setDetails("Wedding reception catering");
        order2.setStatus("PENDING");
        orders.add(order2);
        
        // Add sample staff
        Staff staff1 = new Staff();
        staff1.setId(1);
        staff1.setName("John Smith");
        staff1.setRole("Chef");
        staff1.setAvailable(true);
        staffList.add(staff1);
        
        Staff staff2 = new Staff();
        staff2.setId(2);
        staff2.setName("Jane Doe");
        staff2.setRole("Server");
        staff2.setAvailable(true);
        staffList.add(staff2);
        
        Staff staff3 = new Staff();
        staff3.setId(3);
        staff3.setName("Mike Johnson");
        staff3.setRole("Kitchen Assistant");
        staff3.setAvailable(true);
        staffList.add(staff3);
        
        // Add sample assigned order
        AssignedOrder sampleAssignedOrder = new AssignedOrder();
        sampleAssignedOrder.setId(1);
        sampleAssignedOrder.setOrderId(1);
        sampleAssignedOrder.setStaffId(1);
        sampleAssignedOrder.setStatus("ASSIGNED");
        assignedOrders.add(sampleAssignedOrder);
    }

    // Simple validation without database dependency
    public Manager validateManager(String username, String password) {
        System.out.println("=== SIMPLE OPERATION LOGIN DEBUG ===");
        System.out.println("Attempting to validate manager: " + username);
        
        // Hardcoded credentials for testing
        if ("admin".equals(username) && "admin123".equals(password)) {
            System.out.println("=== LOGIN SUCCESS (HARDCODED) ===");
            Manager manager = new Manager();
            manager.setId(1);
            manager.setUsername("admin");
            manager.setPassword("admin123");
            return manager;
        }
        
        if ("manager".equals(username) && "manager123".equals(password)) {
            System.out.println("=== LOGIN SUCCESS (HARDCODED) ===");
            Manager manager = new Manager();
            manager.setId(2);
            manager.setUsername("manager");
            manager.setPassword("manager123");
            return manager;
        }
        
        System.out.println("=== LOGIN FAILED - INVALID CREDENTIALS ===");
        return null;
    }
    
    // Real methods for dashboard functionality
    public List<Order> getPendingOrders() {
        System.out.println("Getting pending orders: " + orders.size());
        return new ArrayList<>(orders);
    }
    
    public List<Staff> getAvailableStaff() {
        System.out.println("Getting available staff: " + staffList.size());
        return new ArrayList<>(staffList);
    }
    
    public boolean assignOrder(int orderId, int staffId) {
        System.out.println("Assigning order " + orderId + " to staff " + staffId);
        
        // Find the order
        Order order = orders.stream()
            .filter(o -> o.getId() == orderId)
            .findFirst()
            .orElse(null);
            
        if (order == null) {
            System.out.println("Order not found: " + orderId);
            return false;
        }
        
        // Find the staff
        Staff staff = staffList.stream()
            .filter(s -> s.getId() == staffId)
            .findFirst()
            .orElse(null);
            
        if (staff == null) {
            System.out.println("Staff not found: " + staffId);
            return false;
        }
        
        // Create assigned order
        AssignedOrder assignedOrder = new AssignedOrder();
        assignedOrder.setId(assignedOrderIdCounter.getAndIncrement());
        assignedOrder.setOrderId(orderId);
        assignedOrder.setStaffId(staffId);
        assignedOrder.setStatus("ASSIGNED");
        assignedOrders.add(assignedOrder);
        
        // Update order status
        order.setStatus("ASSIGNED");
        
        System.out.println("Order " + orderId + " successfully assigned to staff " + staffId);
        return true;
    }
    
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
    
    // Additional methods for full functionality
    public List<AssignedOrder> getAssignedOrders() {
        System.out.println("Getting assigned orders: " + assignedOrders.size());
        
        // Populate assigned orders with full details
        List<AssignedOrder> populatedAssignedOrders = new ArrayList<>();
        
        for (AssignedOrder assignedOrder : assignedOrders) {
            // Find the original order details
            Order originalOrder = orders.stream()
                .filter(o -> o.getId() == assignedOrder.getOrderId())
                .findFirst()
                .orElse(null);
                
            // Find the staff details
            Staff staff = staffList.stream()
                .filter(s -> s.getId() == assignedOrder.getStaffId())
                .findFirst()
                .orElse(null);
                
            if (originalOrder != null && staff != null) {
                // Create a populated assigned order
                AssignedOrder populatedOrder = new AssignedOrder();
                populatedOrder.setId(assignedOrder.getId());
                populatedOrder.setOrderId(assignedOrder.getOrderId());
                populatedOrder.setCustomerName(originalOrder.getCustomerName());
                populatedOrder.setDetails(originalOrder.getDetails());
                populatedOrder.setStatus(assignedOrder.getStatus());
                populatedOrder.setStaffId(assignedOrder.getStaffId());
                populatedOrder.setStaffName(staff.getName());
                populatedOrder.setStaffRole(staff.getRole());
                populatedOrder.setBookingDate(java.time.LocalDateTime.now().toString());
                
                populatedAssignedOrders.add(populatedOrder);
            }
        }
        
        System.out.println("Returning " + populatedAssignedOrders.size() + " populated assigned orders");
        return populatedAssignedOrders;
    }
    
    public void createOrder(String customerName, String details) {
        System.out.println("Creating order for: " + customerName + " - " + details);
        
        Order newOrder = new Order();
        newOrder.setId(orderIdCounter.getAndIncrement());
        newOrder.setCustomerName(customerName);
        newOrder.setDetails(details);
        newOrder.setStatus("PENDING");
        orders.add(newOrder);
        
        System.out.println("Order created with ID: " + newOrder.getId());
    }
    
    public void deleteOrder(int id) {
        System.out.println("Deleting order: " + id);
        orders.removeIf(order -> order.getId() == id);
        assignedOrders.removeIf(assigned -> assigned.getOrderId() == id);
        System.out.println("Order " + id + " deleted");
    }
    
    public void createStaff(String name, String role) {
        System.out.println("Creating staff: " + name + " - " + role);
        
        Staff newStaff = new Staff();
        newStaff.setId(staffIdCounter.getAndIncrement());
        newStaff.setName(name);
        newStaff.setRole(role);
        newStaff.setAvailable(true);
        staffList.add(newStaff);
        
        System.out.println("Staff created with ID: " + newStaff.getId());
    }
    
    public void updateStaffAvailability(int id, boolean available) {
        System.out.println("Updating staff " + id + " availability: " + available);
        
        staffList.stream()
            .filter(staff -> staff.getId() == id)
            .findFirst()
            .ifPresent(staff -> {
                staff.setAvailable(available);
                System.out.println("Staff " + id + " availability updated to: " + available);
            });
    }
    
    public void deleteStaff(int id) {
        System.out.println("Deleting staff: " + id);
        staffList.removeIf(staff -> staff.getId() == id);
        assignedOrders.removeIf(assigned -> assigned.getStaffId() == id);
        System.out.println("Staff " + id + " deleted");
    }
    
    public void deleteAssignedOrder(int orderId) {
        System.out.println("Deleting assigned order: " + orderId);
        assignedOrders.removeIf(assigned -> assigned.getOrderId() == orderId);
        
        // Update order status back to PENDING
        orders.stream()
            .filter(order -> order.getId() == orderId)
            .findFirst()
            .ifPresent(order -> order.setStatus("PENDING"));
            
        System.out.println("Assigned order " + orderId + " deleted");
    }
}
