package com.example.catering_system.operationManager.Entity;

public class Order {
    private int id;
    private String customerName;
    private String details;
    private String status;

    public Order () {}

    public Order(int id, String customerName, String details, String status) {
        this.id = id;
        this.customerName = customerName;
        this.details = details;
        this.status = status;
    }

    // getter and setter method

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
