package com.example.catering_system.delivery.models;


import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long driverId;
    private String name;
    private String phone;
    private String vehicleType; // e.g., "VAN", "MOTORBIKE"
    private String licenseNumber;
    private boolean isAvailable = true;
    private String lastKnownLocation;

    public Driver() {
    }

    public Driver(String name, String phone, String vehicleType, String licenseNumber) {
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.licenseNumber = licenseNumber;
    }

    // Getters & Setters
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String location) {
        lastKnownLocation = location;
    }
}