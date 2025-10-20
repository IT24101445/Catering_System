package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.Revenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    
    List<Revenue> findByRevenueDateBetween(Date startDate, Date endDate);
    
    List<Revenue> findBySource(String source);
    
    List<Revenue> findByStatus(String status);
    
    @Query("SELECT SUM(r.amount) FROM Revenue r WHERE r.revenueDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT SUM(r.amount) FROM Revenue r WHERE r.source = :source AND r.revenueDate BETWEEN :startDate AND :endDate")
    Double getRevenueBySourceAndDateRange(@Param("source") String source, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
