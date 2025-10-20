package com.example.catering_system.customer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.catering_system.customer.model.MenuItem;
import com.example.catering_system.customer.service.MenuItemService;

/**
 * Controller for Menu Management
 */
@Controller("customerMenuController")
@RequestMapping("/customer/menu")
public class MenuController {
    
    @Autowired
    private MenuItemService menuItemService;
    
    @GetMapping
    public String menuPage(Model model) {
        List<MenuItem> menuItems = menuItemService.findAllMenuItems();
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("categories", MenuItem.MenuCategory.values());
        return "menu-customer";
    }
    
    // API Endpoints for AJAX calls
    @GetMapping("/api/items")
    @ResponseBody
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        List<MenuItem> items = menuItemService.findAllMenuItems();
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        Optional<MenuItem> item = menuItemService.findById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/api/items")
    @ResponseBody
    public ResponseEntity<?> createMenuItem(@RequestBody MenuItem menuItem) {
        try {
            MenuItem createdItem = menuItemService.createMenuItem(menuItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating menu item: " + e.getMessage());
        }
    }
    
    @PutMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItem) {
        try {
            menuItem.setId(id);
            MenuItem updatedItem = menuItemService.updateMenuItem(menuItem);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating menu item: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/api/items/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return ResponseEntity.ok().body("Menu item deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting menu item: " + e.getMessage());
        }
    }
    
    @PutMapping("/api/items/{id}/toggle-availability")
    @ResponseBody
    public ResponseEntity<?> toggleAvailability(@PathVariable Long id) {
        try {
            MenuItem item = menuItemService.toggleAvailability(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling availability: " + e.getMessage());
        }
    }
    
    @PutMapping("/api/items/{id}/toggle-featured")
    @ResponseBody
    public ResponseEntity<?> toggleFeatured(@PathVariable Long id) {
        try {
            MenuItem item = menuItemService.toggleFeatured(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error toggling featured status: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/items/category/{category}")
    @ResponseBody
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable MenuItem.MenuCategory category) {
        List<MenuItem> items = menuItemService.findAvailableMenuItemsByCategory(category);
        return ResponseEntity.ok(items);
    }
}
