package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    List<Expense> findByExpenseDateBetween(Date startDate, Date endDate);
    
    List<Expense> findByCategory(String category);
    
    List<Expense> findByStatus(String status);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    Double getTotalExpensesByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.category = :category AND e.expenseDate BETWEEN :startDate AND :endDate")
    Double getExpensesByCategoryAndDateRange(@Param("category") String category, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
