package com.example.catering_system.customer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catering_system.customer.model.MenuItem;
import com.example.catering_system.customer.repository.MenuItemRepository;

/**
 * Service class for Menu Item management
 */
@Service
@Transactional
public class MenuItemService {
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    public MenuItem createMenuItem(MenuItem menuItem) {
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItem.setUpdatedAt(LocalDateTime.now());
        if (menuItem.getIsAvailable() == null) {
            menuItem.setIsAvailable(true);
        }
        return menuItemRepository.save(menuItem);
    }
    
    public MenuItem updateMenuItem(MenuItem menuItem) {
        MenuItem existingItem = menuItemRepository.findById(menuItem.getId())
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        
        // Update fields
        existingItem.setName(menuItem.getName());
        existingItem.setDescription(menuItem.getDescription());
        existingItem.setShortDescription(menuItem.getShortDescription());
        existingItem.setPrice(menuItem.getPrice());
        existingItem.setCostPrice(menuItem.getCostPrice());
        existingItem.setCategory(menuItem.getCategory());
        existingItem.setCuisineType(menuItem.getCuisineType());
        existingItem.setPreparationTimeMinutes(menuItem.getPreparationTimeMinutes());
        existingItem.setServingSize(menuItem.getServingSize());
        existingItem.setCaloriesPerServing(menuItem.getCaloriesPerServing());
        existingItem.setIngredients(menuItem.getIngredients());
        existingItem.setAllergens(menuItem.getAllergens());
        existingItem.setDietaryInfo(menuItem.getDietaryInfo());
        existingItem.setSpiceLevel(menuItem.getSpiceLevel());
        existingItem.setImageUrl(menuItem.getImageUrl());
        existingItem.setIsAvailable(menuItem.getIsAvailable());
        existingItem.setIsFeatured(menuItem.getIsFeatured());
        existingItem.setDisplayOrder(menuItem.getDisplayOrder());
        existingItem.setUpdatedAt(LocalDateTime.now());
        
        return menuItemRepository.save(existingItem);
    }
    
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        menuItem.setIsAvailable(false);
        menuItem.setUpdatedAt(LocalDateTime.now());
        menuItemRepository.save(menuItem);
    }
    
    public Optional<MenuItem> findById(Long id) {
        return menuItemRepository.findById(id);
    }
    
    public List<MenuItem> findAllMenuItems() {
        return menuItemRepository.findAll();
    }
    
    public List<MenuItem> findAvailableMenuItems() {
        return menuItemRepository.findByIsAvailableTrue();
    }
    
    public List<MenuItem> findFeaturedMenuItems() {
        return menuItemRepository.findByIsFeaturedTrue();
    }
    
    public List<MenuItem> findMenuItemsByCategory(MenuItem.MenuCategory category) {
        return menuItemRepository.findByCategory(category);
    }
    
    public List<MenuItem> findAvailableMenuItemsByCategory(MenuItem.MenuCategory category) {
        return menuItemRepository.findAvailableByCategoryOrderByDisplay(category);
    }
    
    public List<MenuItem> searchMenuItems(String searchTerm) {
        return menuItemRepository.searchAvailableItems(searchTerm);
    }
    
    public List<MenuItem> findMenuItemsByDietaryInfo(String dietaryInfo) {
        return menuItemRepository.findAvailableByDietaryInfo(dietaryInfo);
    }
    
    public List<MenuItem> findMenuItemsWithoutAllergen(String allergen) {
        return menuItemRepository.findAvailableWithoutAllergen(allergen);
    }
    
    public MenuItem toggleAvailability(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        menuItem.setIsAvailable(!menuItem.getIsAvailable());
        menuItem.setUpdatedAt(LocalDateTime.now());
        return menuItemRepository.save(menuItem);
    }
    
    public MenuItem toggleFeatured(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        menuItem.setIsFeatured(!menuItem.getIsFeatured());
        menuItem.setUpdatedAt(LocalDateTime.now());
        return menuItemRepository.save(menuItem);
    }
    
    public long getTotalMenuItems() {
        return menuItemRepository.count();
    }
}
