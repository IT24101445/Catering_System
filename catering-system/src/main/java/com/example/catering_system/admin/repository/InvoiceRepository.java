package com.example.catering_system.admin.repository;

import com.example.catering_system.admin.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
