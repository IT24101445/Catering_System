
package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.StaffPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffPaymentRepository extends JpaRepository<StaffPayment, Long> {
    // Custom queries if needed
}
