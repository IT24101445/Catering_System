package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.EventBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventBudgetRepository extends JpaRepository<EventBudget, Long> {
}
