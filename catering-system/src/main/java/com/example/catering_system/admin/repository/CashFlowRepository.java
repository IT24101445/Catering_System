package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.CashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
    
    List<CashFlow> findByFlowDateBetween(Date startDate, Date endDate);
    
    List<CashFlow> findByType(String type);
    
    List<CashFlow> findByCategory(String category);
    
    @Query("SELECT SUM(cf.amount) FROM CashFlow cf WHERE cf.type = :type AND cf.flowDate BETWEEN :startDate AND :endDate")
    Double getTotalCashFlowByTypeAndDateRange(@Param("type") String type, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT SUM(cf.amount) FROM CashFlow cf WHERE cf.flowDate BETWEEN :startDate AND :endDate")
    Double getNetCashFlowByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
