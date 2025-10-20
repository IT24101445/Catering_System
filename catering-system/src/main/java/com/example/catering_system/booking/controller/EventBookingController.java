package com.example.catering_system.booking.controller;

import com.example.catering_system.booking.entity.EventBooking;
import com.example.catering_system.booking.service.EventBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class EventBookingController {
    
    @Autowired
    private EventBookingService bookingService;
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody EventBooking booking) {
        Map<String, Object> response = new HashMap<>();
        try {
            EventBooking savedBooking = bookingService.createBooking(booking);
            response.put("success", true);
            response.put("message", "Booking created successfully!");
            response.put("bookingId", savedBooking.getId());
            response.put("booking", savedBooking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<EventBooking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EventBooking> getBookingById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.getBookingById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<EventBooking>> getBookingsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<EventBooking> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam EventBooking.BookingStatus status) {
        try {
            return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Map<String, Object>> confirmBooking(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            EventBooking booking = bookingService.updateBookingStatus(id, EventBooking.BookingStatus.CONFIRMED);
            response.put("success", true);
            response.put("message", "Booking confirmed successfully");
            response.put("booking", booking);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            bookingService.deleteBooking(id);
            response.put("success", true);
            response.put("message", "Booking deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
