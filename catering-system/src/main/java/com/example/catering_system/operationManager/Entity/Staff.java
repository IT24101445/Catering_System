package com.example.catering_system.operationManager.Entity;

public class Staff {
    private int id;
    private String name;
    private String role;
    private boolean available;

    public Staff() {}

    public Staff(int id, String name, String role, boolean available) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.available = available;
    }

    // getter and setter method
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
