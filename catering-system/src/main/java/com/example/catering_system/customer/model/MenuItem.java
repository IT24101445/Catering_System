package com.example.catering_system.customer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Menu Item Entity for Restaurant Menu Management
 */
@Entity(name = "CustomerMenuItem")
@Table(name = "menu_items")
public class MenuItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Item name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", length = 2000)
    private String description;
    
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private MenuCategory category;
    
    @Column(name = "cuisine_type")
    private String cuisineType;
    
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes;
    
    @Column(name = "serving_size")
    private String servingSize;
    
    @Column(name = "calories_per_serving")
    private Integer caloriesPerServing;
    
    @Column(name = "ingredients", length = 2000)
    private String ingredients;
    
    @Column(name = "allergens", length = 500)
    private String allergens;
    
    @Column(name = "dietary_info", length = 500)
    private String dietaryInfo; // VEGETARIAN, VEGAN, GLUTEN_FREE, etc.
    
    @Column(name = "spice_level")
    private String spiceLevel; // MILD, MEDIUM, HOT, EXTRA_HOT
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Enums
    public enum MenuCategory {
        APPETIZER, MAIN_COURSE, DESSERT, BEVERAGE, SALAD, SOUP, 
        BREAKFAST, LUNCH, DINNER, SNACK, CATERING_PACKAGE
    }
    
    // Constructors
    public MenuItem() {}
    
    public MenuItem(String name, String description, BigDecimal price, MenuCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    
    public MenuCategory getCategory() { return category; }
    public void setCategory(MenuCategory category) { this.category = category; }
    
    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }
    
    public Integer getPreparationTimeMinutes() { return preparationTimeMinutes; }
    public void setPreparationTimeMinutes(Integer preparationTimeMinutes) { this.preparationTimeMinutes = preparationTimeMinutes; }
    
    public String getServingSize() { return servingSize; }
    public void setServingSize(String servingSize) { this.servingSize = servingSize; }
    
    public Integer getCaloriesPerServing() { return caloriesPerServing; }
    public void setCaloriesPerServing(Integer caloriesPerServing) { this.caloriesPerServing = caloriesPerServing; }
    
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    
    public String getAllergens() { return allergens; }
    public void setAllergens(String allergens) { this.allergens = allergens; }
    
    public String getDietaryInfo() { return dietaryInfo; }
    public void setDietaryInfo(String dietaryInfo) { this.dietaryInfo = dietaryInfo; }
    
    public String getSpiceLevel() { return spiceLevel; }
    public void setSpiceLevel(String spiceLevel) { this.spiceLevel = spiceLevel; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility methods
    public BigDecimal getProfitMargin() {
        if (costPrice != null && price != null) {
            return price.subtract(costPrice);
        }
        return BigDecimal.ZERO;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
