package com.example.catering_system.delivery.dto.Driver;


public class Create {
    private String email;
    private String name;
    private String status; // optional; if null/blank, service will default to "AVAILABLE"

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}