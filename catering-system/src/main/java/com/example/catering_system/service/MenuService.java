package com.example.catering_system.service;

import com.example.catering_system.model.MenuItem;
import com.example.catering_system.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findByArchivedFalse();
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getByCuisine(String cuisine) {
        return menuItemRepository.findByCuisineIgnoreCase(cuisine);
    }

    public List<MenuItem> getByEventType(String eventType) {
        return menuItemRepository.findByEventTypeIgnoreCase(eventType);
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.findById(id).ifPresent(item -> {
            item.setArchived(true);
            menuItemRepository.save(item);
        });
    }

    // Inventory operations
    public void updateStock(Long id, int newStockLevel) {
        menuItemRepository.findById(id).ifPresent(item -> {
            item.setStockLevel(newStockLevel);
            item.setAvailable(newStockLevel > 0);
            menuItemRepository.save(item);
        });
    }

    public void toggleAvailability(Long id, boolean available) {
        menuItemRepository.findById(id).ifPresent(item -> {
            item.setAvailable(available);
            menuItemRepository.save(item);
        });
    }
}
