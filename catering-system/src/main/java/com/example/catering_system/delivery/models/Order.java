package com.example.catering_system.delivery.models;


import jakarta.persistence.*;

@Entity
@Table(name = "orders") // avoid SQL reserved word "ORDER"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 160)
    private String customerName;

    @Column(name = "pickup_address", nullable = false, length = 500)
    private String pickupAddress;

    @Column(name = "dropoff_address", nullable = false, length = 500)
    private String dropoffAddress;

    public Order() {
    }

    public Order(Long id, String customerName, String pickupAddress, String dropoffAddress) {
        this.id = id;
        this.customerName = customerName;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }
}