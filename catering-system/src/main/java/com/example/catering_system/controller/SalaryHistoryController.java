package com.example.catering_system.controller;

import com.example.catering_system.model.StaffPayment;
import com.example.catering_system.service.StaffPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class SalaryHistoryController {

    @Autowired
    private StaffPaymentService staffPaymentService;

    @GetMapping("/salary-history")
    public String salaryHistory(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Integer month,
                                Model model) {
        LocalDate now = LocalDate.now();
        int y = year != null ? year : now.getYear();
        int m = month != null ? month : now.getMonthValue();
        LocalDate start = LocalDate.of(y, m, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        Date startDate = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(end.atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant());

        List<StaffPayment> payments = staffPaymentService.getPaymentsBetween(startDate, endDate);
        double total = payments.stream().mapToDouble(StaffPayment::getAmountPaid).sum();

        model.addAttribute("year", y);
        model.addAttribute("month", m);
        model.addAttribute("payments", payments);
        model.addAttribute("total", total);
        return "salary-history";
    }
}
