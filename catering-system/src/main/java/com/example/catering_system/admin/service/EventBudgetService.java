package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.EventBudget;
import com.example.catering_system.admin.repository.EventBudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventBudgetService {

    @Autowired
    private EventBudgetRepository eventBudgetRepository;

    // Get all event budgets
    public List<EventBudget> getAllEventBudgets() {
        return eventBudgetRepository.findAll();
    }

    // Save or update event budget
    public EventBudget saveEventBudget(EventBudget eventBudget) {
        return eventBudgetRepository.save(eventBudget);
    }
}
