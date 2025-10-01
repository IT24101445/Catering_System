package com.example.catering_system.service;

import com.example.catering_system.model.StaffPayment;
import com.example.catering_system.repository.StaffPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffPaymentService {

    @Autowired
    private StaffPaymentRepository staffPaymentRepository;

    // Get all staff payments
    public List<StaffPayment> getAllStaffPayments() {
        return staffPaymentRepository.findAll();
    }

    // Get a single staff payment by ID
    public Optional<StaffPayment> getStaffPaymentById(Long id) {
        return staffPaymentRepository.findById(id);
    }

    // Add a new staff payment
    public StaffPayment addStaffPayment(StaffPayment staffPayment) {
        return staffPaymentRepository.save(staffPayment);
    }

    // Update an existing staff payment
    public StaffPayment updateStaffPayment(Long id, StaffPayment staffPaymentDetails) {
        Optional<StaffPayment> existingPayment = staffPaymentRepository.findById(id);
        if (existingPayment.isPresent()) {
            StaffPayment staffPayment = existingPayment.get();
            staffPayment.setEmployee(staffPaymentDetails.getEmployee());
            staffPayment.setAmountPaid(staffPaymentDetails.getAmountPaid());
            staffPayment.setPaymentDate(staffPaymentDetails.getPaymentDate());
            staffPayment.setPaymentMethod(staffPaymentDetails.getPaymentMethod());
            staffPayment.setStatus(staffPaymentDetails.getStatus());
            staffPayment.setNotes(staffPaymentDetails.getNotes());
            return staffPaymentRepository.save(staffPayment);
        }
        return null;
    }

    // Delete a staff payment by ID
    public void deleteStaffPayment(Long id) {
        staffPaymentRepository.deleteById(id);
    }

    public java.util.List<StaffPayment> getPaymentsBetween(java.util.Date start, java.util.Date end) {
        return staffPaymentRepository.findByPaymentDateBetween(start, end);
    }

    public java.util.List<StaffPayment> getPaymentsByYearMonth(Integer year, Integer month) {
        return staffPaymentRepository.findByPaymentYearAndPaymentMonth(year, month);
    }
}
