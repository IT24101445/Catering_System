package com.example.catering_system.customer.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Enhanced Customer Entity with Advanced Features
 */
@Entity
@Table(name = "customers")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Email(message = "Please provide a valid email address")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 20, message = "Phone must be between 10 and 20 characters")
    private String phone;
    
    @Column(name = "secondary_phone")
    private String secondaryPhone;
    
    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    private CustomerType customerType = CustomerType.INDIVIDUAL;
    
    @Column(name = "company_name")
    private String companyName;
    
    @Column(name = "tax_id")
    private String taxId;
    
    // Address Information
    @Column(name = "primary_address", length = 500)
    private String primaryAddress;
    
    @Column(name = "billing_address", length = 500)
    private String billingAddress;
    
    @Column(name = "delivery_instructions", length = 1000)
    private String deliveryInstructions;
    
    // Preferences and Dietary Information
    @Column(name = "dietary_preferences", length = 500)
    private String dietaryPreferences;
    
    @Column(name = "allergies", length = 500)
    private String allergies;
    
    @Column(name = "preferred_cuisine", length = 200)
    private String preferredCuisine;
    
    // Loyalty and Financial Information
    @Column(name = "loyalty_points", precision = 10, scale = 2)
    private BigDecimal loyaltyPoints = BigDecimal.ZERO;
    
    @Column(name = "total_spent", precision = 12, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;
    
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;
    
    @Column(name = "outstanding_balance", precision = 10, scale = 2)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;
    
    // Status and Tracking
    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();
    
    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;
    
    @Column(name = "preferred_contact_method")
    private String preferredContactMethod = "EMAIL";
    
    @Column(name = "marketing_consent")
    private Boolean marketingConsent = false;
    
    @Column(name = "notes", length = 2000)
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Relationships
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CustomerAddress> addresses = new ArrayList<>();
    
    // Enums
    public enum CustomerType {
        INDIVIDUAL, CORPORATE, RESTAURANT, HOTEL, EVENT_PLANNER
    }
    
    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED, VIP, BLACKLISTED
    }
    
    // Constructors
    public Customer() {}
    
    public Customer(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getSecondaryPhone() { return secondaryPhone; }
    public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }
    
    public LocalDateTime getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDateTime dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public CustomerType getCustomerType() { return customerType; }
    public void setCustomerType(CustomerType customerType) { this.customerType = customerType; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }
    
    public String getPrimaryAddress() { return primaryAddress; }
    public void setPrimaryAddress(String primaryAddress) { this.primaryAddress = primaryAddress; }
    
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    
    public String getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(String dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getPreferredCuisine() { return preferredCuisine; }
    public void setPreferredCuisine(String preferredCuisine) { this.preferredCuisine = preferredCuisine; }
    
    public BigDecimal getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(BigDecimal loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    
    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
    
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }
    
    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }
    
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    
    public LocalDateTime getLastOrderDate() { return lastOrderDate; }
    public void setLastOrderDate(LocalDateTime lastOrderDate) { this.lastOrderDate = lastOrderDate; }
    
    public String getPreferredContactMethod() { return preferredContactMethod; }
    public void setPreferredContactMethod(String preferredContactMethod) { this.preferredContactMethod = preferredContactMethod; }
    
    public Boolean getMarketingConsent() { return marketingConsent; }
    public void setMarketingConsent(Boolean marketingConsent) { this.marketingConsent = marketingConsent; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    
    public List<CustomerAddress> getAddresses() { return addresses; }
    public void setAddresses(List<CustomerAddress> addresses) { this.addresses = addresses; }
    
    // Utility methods
    public void addLoyaltyPoints(BigDecimal points) {
        this.loyaltyPoints = this.loyaltyPoints.add(points);
    }
    
    public void addToTotalSpent(BigDecimal amount) {
        this.totalSpent = this.totalSpent.add(amount);
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
