package com.example.catering_system.customer.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Permission Entity for Fine-grained Access Control
 */
@Entity
@Table(name = "permissions")
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Permission name is required")
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "resource_type")
    private String resourceType; // CUSTOMER, ORDER, MENU, INVENTORY, etc.
    
    @Column(name = "action_type")
    private String actionType; // CREATE, READ, UPDATE, DELETE, MANAGE
    
    // Many-to-Many relationship with Role
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
    
    // Constructors
    public Permission() {}
    
    public Permission(String name, String description, String resourceType, String actionType) {
        this.name = name;
        this.description = description;
        this.resourceType = resourceType;
        this.actionType = actionType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }
    
    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }
    
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}
