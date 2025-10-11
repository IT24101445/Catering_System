package com.example.catering_system.delivery.dto.Delivery;



public class Update {
    private String pickupAddress;   // optional
    private String dropoffAddress;  // optional
    private String status;          // optional

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDropoffAddress() { return dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}