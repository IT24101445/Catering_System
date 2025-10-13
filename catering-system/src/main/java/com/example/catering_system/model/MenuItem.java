package com.example.catering_system.model;

import jakarta.persistence.*;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "price")
    private double price;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "cuisine")
    private String cuisine; // e.g., Indian, Italian
    
    @Column(name = "event_type")
    private String eventType; // e.g., Wedding, Corporate
    
    @Column(name = "archived")
    private Boolean archived = false;
    
    @Column(name = "dishes_included")
    private String dishesIncluded; // comma-separated dishes

    // Inventory tracking
    @Column(name = "stock_level")
    private Integer stockLevel;

    @Column(name = "available")
    private Boolean available = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getDishesIncluded() {
        return dishesIncluded;
    }

    public void setDishesIncluded(String dishesIncluded) {
        this.dishesIncluded = dishesIncluded;
    }
    
    public Integer getStockLevel() { return stockLevel; }
    public void setStockLevel(Integer stockLevel) { this.stockLevel = stockLevel; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    // Getters and Setters
}
