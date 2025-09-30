package com.example.catering_system.repository;

import com.example.catering_system.model.EventBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventBudgetRepository extends JpaRepository<EventBudget, Long> {
}
