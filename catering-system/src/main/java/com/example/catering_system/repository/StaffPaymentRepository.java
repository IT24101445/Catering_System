
package com.example.catering_system.repository;

import com.example.catering_system.model.StaffPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffPaymentRepository extends JpaRepository<StaffPayment, Long> {

    java.util.List<StaffPayment> findByPaymentDateBetween(java.util.Date startInclusive, java.util.Date endInclusive);

    java.util.List<StaffPayment> findByPaymentYearAndPaymentMonth(Integer paymentYear, Integer paymentMonth);

    java.util.List<StaffPayment> findByEmployeeIdOrderByPaymentDateDesc(Long employeeId);
}
