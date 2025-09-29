package com.example.catering_system.delivery.dto.Delivery;


public class Response {
    private Long id;
    private String pickupAddress;
    private String dropoffAddress;
    private String status;
    // Convenience field for map integrations (computed in controller)
    private String directionsUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDropoffAddress() { return dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDirectionsUrl() { return directionsUrl; }
    public void setDirectionsUrl(String directionsUrl) { this.directionsUrl = directionsUrl; }
}