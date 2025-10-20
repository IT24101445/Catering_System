package com.example.catering_system.customer.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Customer Address Entity for Multiple Address Support
 */
@Entity
@Table(name = "customer_addresses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CustomerAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
    
    @NotBlank(message = "Address type is required")
    @Column(name = "address_type")
    private String addressType; // HOME, OFFICE, BILLING, DELIVERY
    
    @NotBlank(message = "Street address is required")
    @Column(name = "street_address", nullable = false, length = 500)
    private String streetAddress;
    
    @Column(name = "apartment_unit")
    private String apartmentUnit;
    
    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;
    
    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;
    
    @NotBlank(message = "Postal code is required")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    
    @Column(nullable = false)
    private String country = "USA";
    
    @Column(name = "delivery_instructions", length = 1000)
    private String deliveryInstructions;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public CustomerAddress() {}
    
    public CustomerAddress(Customer customer, String addressType, String streetAddress, 
                          String city, String state, String postalCode) {
        this.customer = customer;
        this.addressType = addressType;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
    
    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }
    
    public String getApartmentUnit() { return apartmentUnit; }
    public void setApartmentUnit(String apartmentUnit) { this.apartmentUnit = apartmentUnit; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Utility method
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        address.append(streetAddress);
        if (apartmentUnit != null && !apartmentUnit.isEmpty()) {
            address.append(", ").append(apartmentUnit);
        }
        address.append(", ").append(city);
        address.append(", ").append(state);
        address.append(" ").append(postalCode);
        address.append(", ").append(country);
        return address.toString();
    }
}
