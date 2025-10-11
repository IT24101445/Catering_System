package com.example.catering_system.repository;

import com.example.catering_system.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    java.util.List<MenuItem> findByCuisineIgnoreCase(String cuisine);
    java.util.List<MenuItem> findByEventTypeIgnoreCase(String eventType);
    java.util.List<MenuItem> findByArchivedFalse();
}
