package com.example.catering_system.kitchen.service;

import com.example.catering_system.kitchen.entity.KitchenStaff;
import com.example.catering_system.kitchen.repository.KitchenStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KitchenService {
    
    @Autowired
    private KitchenStaffRepository kitchenStaffRepository;
    
    // Register a new kitchen staff member
    public KitchenStaff registerKitchenStaff(String email, String password, String firstName, String lastName, String position) {
        // Check if a staff member already exists
        if (kitchenStaffRepository.existsByEmail(email)) {
            throw new RuntimeException("Kitchen staff with email " + email + " already exists");
        }
        
        // Create a new kitchen staff member
        KitchenStaff staff = new KitchenStaff(email, password, firstName, lastName, position);
        return kitchenStaffRepository.save(staff);
    }
    
    // Authenticate kitchen staff login
    public Optional<KitchenStaff> authenticateKitchenStaff(String email, String password) {
        Optional<KitchenStaff> staff = kitchenStaffRepository.findByEmailAndPasswordAndIsActiveTrue(email, password);
        if (staff.isPresent()) {
            // Update last login time
            staff.get().setLastLogin(LocalDateTime.now());
            kitchenStaffRepository.save(staff.get());
        }
        return staff;
    }
    
    // Get kitchen staff by email
    public Optional<KitchenStaff> getKitchenStaffByEmail(String email) {
        return kitchenStaffRepository.findByEmailAndIsActiveTrue(email);
    }
    
    // Get kitchen staff by ID
    public Optional<KitchenStaff> getKitchenStaffById(Long id) {
        return kitchenStaffRepository.findByIdAndIsActiveTrue(id);
    }
    
    // Get all active kitchen staff
    public List<KitchenStaff> getAllActiveKitchenStaff() {
        return kitchenStaffRepository.findAll().stream()
                .filter(staff -> staff.getIsActive())
                .toList();
    }
    
    // Update kitchen staff
    public KitchenStaff updateKitchenStaff(KitchenStaff staff) {
        staff.setUpdatedAt(LocalDateTime.now());
        return kitchenStaffRepository.save(staff);
    }
    
    // Deactivate kitchen staff (soft delete)
    public void deactivateKitchenStaff(Long id) {
        Optional<KitchenStaff> staff = kitchenStaffRepository.findById(id);
        if (staff.isPresent()) {
            staff.get().setIsActive(false);
            staff.get().setUpdatedAt(LocalDateTime.now());
            kitchenStaffRepository.save(staff.get());
        }
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        return kitchenStaffRepository.existsByEmail(email);
    }
}
