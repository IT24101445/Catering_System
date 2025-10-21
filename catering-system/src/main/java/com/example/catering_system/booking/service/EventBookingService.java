package com.example.catering_system.booking.service;

import com.example.catering_system.booking.entity.EventBooking;
import com.example.catering_system.booking.repository.EventBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventBookingService {
    
    @Autowired
    private EventBookingRepository bookingRepository;
    
    private static final BigDecimal BASE_COST_PER_PERSON = new BigDecimal("25.00");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");
    
    public EventBooking createBooking(EventBooking booking) {
        // Calculate costs
        calculateCosts(booking);
        
        // Set timestamps
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        
        // Save and return
        return bookingRepository.save(booking);
    }
    
    private void calculateCosts(EventBooking booking) {
        // Base cost
        BigDecimal baseCost = BASE_COST_PER_PERSON.multiply(new BigDecimal(booking.getGuestCount()));
        booking.setBaseCost(baseCost);
        
        // Services cost (already calculated from frontend)
        if (booking.getServicesCost() == null) {
            booking.setServicesCost(BigDecimal.ZERO);
        }
        
        // Calculate tax
        BigDecimal subtotal = baseCost.add(booking.getServicesCost());
        BigDecimal taxAmount = subtotal.multiply(TAX_RATE);
        booking.setTaxAmount(taxAmount);
        
        // Calculate total
        BigDecimal totalCost = subtotal.add(taxAmount);
        booking.setTotalCost(totalCost);
    }
    
    public List<EventBooking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public List<EventBooking> getConfirmedBookings() {
        return bookingRepository.findByStatus(EventBooking.BookingStatus.CONFIRMED);
    }
    
    public EventBooking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
    }
    
    public List<EventBooking> getBookingsByEmail(String email) {
        return bookingRepository.findByEmail(email);
    }
    
    public EventBooking updateBookingStatus(Long id, EventBooking.BookingStatus status) {
        EventBooking booking = getBookingById(id);
        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
