package com.example.catering_system.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.catering_system.customer.model.MenuItem;

/**
 * Repository interface for MenuItem entity
 */
@Repository("customerMenuItemRepository")
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    List<MenuItem> findByIsAvailableTrue();
    
    List<MenuItem> findByIsFeaturedTrue();
    
    List<MenuItem> findByCategory(MenuItem.MenuCategory category);
    
    List<MenuItem> findByCuisineType(String cuisineType);
    
    @Query("SELECT m FROM CustomerMenuItem m WHERE m.isAvailable = true AND m.category = :category ORDER BY m.displayOrder")
    List<MenuItem> findAvailableByCategoryOrderByDisplay(@Param("category") MenuItem.MenuCategory category);
    
    @Query("SELECT m FROM CustomerMenuItem m WHERE m.isAvailable = true AND m.isFeatured = true ORDER BY m.displayOrder")
    List<MenuItem> findFeaturedAvailableItems();
    
    @Query("SELECT m FROM CustomerMenuItem m WHERE m.isAvailable = true AND (m.name LIKE %:searchTerm% OR m.description LIKE %:searchTerm%)")
    List<MenuItem> searchAvailableItems(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT m FROM CustomerMenuItem m WHERE m.isAvailable = true AND m.dietaryInfo LIKE %:dietaryInfo%")
    List<MenuItem> findAvailableByDietaryInfo(@Param("dietaryInfo") String dietaryInfo);
    
    @Query("SELECT m FROM CustomerMenuItem m WHERE m.isAvailable = true AND m.allergens NOT LIKE %:allergen%")
    List<MenuItem> findAvailableWithoutAllergen(@Param("allergen") String allergen);
}
