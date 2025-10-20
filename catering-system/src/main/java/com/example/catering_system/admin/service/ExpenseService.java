package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.Expense;
import com.example.catering_system.admin.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByDateRange(Date startDate, Date endDate) {
        return expenseRepository.findByExpenseDateBetween(startDate, endDate);
    }

    public List<Expense> getExpensesByCategory(String category) {
        return expenseRepository.findByCategory(category);
    }

    public List<Expense> getExpensesByStatus(String status) {
        return expenseRepository.findByStatus(status);
    }

    public Double getTotalExpensesByDateRange(Date startDate, Date endDate) {
        Double total = expenseRepository.getTotalExpensesByDateRange(startDate, endDate);
        return total != null ? total : 0.0;
    }

    public Double getExpensesByCategoryAndDateRange(String category, Date startDate, Date endDate) {
        Double total = expenseRepository.getExpensesByCategoryAndDateRange(category, startDate, endDate);
        return total != null ? total : 0.0;
    }

    // Categorized expense analysis
    public Double getIngredientCosts(Date startDate, Date endDate) {
        return getExpensesByCategoryAndDateRange("INGREDIENTS", startDate, endDate);
    }

    public Double getUtilityCosts(Date startDate, Date endDate) {
        return getExpensesByCategoryAndDateRange("UTILITIES", startDate, endDate);
    }

    public Double getTransportationCosts(Date startDate, Date endDate) {
        return getExpensesByCategoryAndDateRange("TRANSPORTATION", startDate, endDate);
    }

    public Double getEquipmentCosts(Date startDate, Date endDate) {
        return getExpensesByCategoryAndDateRange("EQUIPMENT", startDate, endDate);
    }

    // Delete methods
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public void deleteAllExpenses() {
        expenseRepository.deleteAll();
    }
}
