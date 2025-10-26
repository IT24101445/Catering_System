// Simple test to verify operation manager login
// This can be run to test the login functionality

import com.example.catering_system.operationManager.Service.DatabaseService;
import com.example.catering_system.operationManager.Entity.Manager;

public class TestOperationApp {
    public static void main(String[] args) {
        System.out.println("=== TESTING OPERATION MANAGER LOGIN ===");
        
        DatabaseService dbService = new DatabaseService();
        
        // Test 1: Admin login
        System.out.println("\n1. Testing admin login...");
        Manager admin = dbService.validateManager("admin", "admin123");
        if (admin != null) {
            System.out.println("✓ Admin login successful: " + admin.getUsername());
        } else {
            System.out.println("❌ Admin login failed");
        }
        
        // Test 2: Manager login
        System.out.println("\n2. Testing manager login...");
        Manager manager = dbService.validateManager("manager", "manager123");
        if (manager != null) {
            System.out.println("✓ Manager login successful: " + manager.getUsername());
        } else {
            System.out.println("❌ Manager login failed");
        }
        
        // Test 3: Invalid login
        System.out.println("\n3. Testing invalid login...");
        Manager invalid = dbService.validateManager("invalid", "invalid");
        if (invalid == null) {
            System.out.println("✓ Invalid login correctly rejected");
        } else {
            System.out.println("❌ Invalid login incorrectly accepted");
        }
        
        System.out.println("\n=== TEST COMPLETE ===");
    }
}
