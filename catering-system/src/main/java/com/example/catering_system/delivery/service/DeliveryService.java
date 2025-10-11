package com.example.catering_system.delivery.service;



import com.example.catering_system.delivery.models.Delivery;
import com.example.catering_system.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliveryService {

    private final DeliveryRepository repo;

    public DeliveryService(DeliveryRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @Transactional
    public Delivery create(String pickupAddress, String dropoffAddress, String status) {
        Delivery d = new Delivery();
        d.setPickupAddress(pickupAddress);
        d.setDropoffAddress(dropoffAddress);
        d.setStatus(status == null || status.isBlank() ? "PENDING" : status);
        return repo.save(d);
    }

    // READ (one)
    @Transactional(readOnly = true)
    public Delivery get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Delivery not found"));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<Delivery> list() {
        return repo.findAll();
    }

    // UPDATE (partial)
    @Transactional
    public Delivery update(Long id, String pickupAddress, String dropoffAddress, String status) {
        Delivery d = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Delivery not found"));

        if (pickupAddress != null && !pickupAddress.isBlank()) {
            d.setPickupAddress(pickupAddress);
        }
        if (dropoffAddress != null && !dropoffAddress.isBlank()) {
            d.setDropoffAddress(dropoffAddress);
        }
        if (status != null && !status.isBlank()) {
            d.setStatus(status);
        }
        return repo.save(d);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Delivery not found");
        }
        repo.deleteById(id);
    }
}