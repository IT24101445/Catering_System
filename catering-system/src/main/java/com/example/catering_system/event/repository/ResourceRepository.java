package com.example.catering_system.event.repository;

import com.example.catering_system.event.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    @Modifying
    @Query("DELETE FROM Resource r WHERE r.event.id = :eventId")
    void deleteByEventId(@Param("eventId") Long eventId);
}