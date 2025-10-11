package com.example.catering_system.delivery.dto.DeliverySupervisor;

public class Update {
    private String email;   // optional
    private String password; // optional

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}