package com.example.catering_system.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfitLossService {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private StaffPaymentService staffPaymentService;

    public Map<String, Object> getProfitLossReport(Date startDate, Date endDate) {
        Map<String, Object> report = new HashMap<>();

        // Revenue calculations
        Double totalRevenue = revenueService.getTotalRevenueByDateRange(startDate, endDate);
        Double cateringRevenue = revenueService.getRevenueBySourceAndDateRange("CATERING_ORDER", startDate, endDate);
        Double eventRevenue = revenueService.getRevenueBySourceAndDateRange("EVENT", startDate, endDate);
        Double packageRevenue = revenueService.getRevenueBySourceAndDateRange("PACKAGE", startDate, endDate);
        Double subscriptionRevenue = revenueService.getRevenueBySourceAndDateRange("SUBSCRIPTION", startDate, endDate);

        // Expense calculations
        Double totalExpenses = expenseService.getTotalExpensesByDateRange(startDate, endDate);
        Double ingredientCosts = expenseService.getIngredientCosts(startDate, endDate);
        Double utilityCosts = expenseService.getUtilityCosts(startDate, endDate);
        Double transportationCosts = expenseService.getTransportationCosts(startDate, endDate);
        Double equipmentCosts = expenseService.getEquipmentCosts(startDate, endDate);

        // Staff payment calculations
        Double totalStaffPayments = staffPaymentService.getAllStaffPayments().stream()
                .filter(payment -> payment.getPaymentDate().after(startDate) && payment.getPaymentDate().before(endDate))
                .mapToDouble(payment -> payment.getAmountPaid())
                .sum();

        // Profit calculations
        Double grossProfit = totalRevenue - totalExpenses;
        Double netProfit = grossProfit - totalStaffPayments;
        Double profitMargin = totalRevenue > 0 ? (netProfit / totalRevenue) * 100 : 0.0;

        // Build report
        report.put("totalRevenue", totalRevenue);
        report.put("cateringRevenue", cateringRevenue);
        report.put("eventRevenue", eventRevenue);
        report.put("packageRevenue", packageRevenue);
        report.put("subscriptionRevenue", subscriptionRevenue);
        
        report.put("totalExpenses", totalExpenses);
        report.put("ingredientCosts", ingredientCosts);
        report.put("utilityCosts", utilityCosts);
        report.put("transportationCosts", transportationCosts);
        report.put("equipmentCosts", equipmentCosts);
        report.put("totalStaffPayments", totalStaffPayments);
        
        report.put("grossProfit", grossProfit);
        report.put("netProfit", netProfit);
        report.put("profitMargin", profitMargin);

        return report;
    }

    public Map<String, Object> getMonthlyProfitLoss(int year, int month) {
        Date startDate = new Date(year - 1900, month - 1, 1);
        Date endDate = new Date(year - 1900, month, 0);
        return getProfitLossReport(startDate, endDate);
    }

    public Map<String, Object> getAnnualProfitLoss(int year) {
        Date startDate = new Date(year - 1900, 0, 1);
        Date endDate = new Date(year - 1900, 11, 31);
        return getProfitLossReport(startDate, endDate);
    }

    public Double getCurrentProfitability() {
        Date today = new Date();
        Date thirtyDaysAgo = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        Map<String, Object> report = getProfitLossReport(thirtyDaysAgo, today);
        return (Double) report.get("netProfit");
    }
}
