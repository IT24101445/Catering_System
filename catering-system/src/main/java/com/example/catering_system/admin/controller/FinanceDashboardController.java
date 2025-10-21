package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FinanceDashboardController {

    @Autowired
    private RevenueService revenueService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private ProfitLossService profitLossService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private StaffPaymentService staffPaymentService;

    @GetMapping("/admin/finance-dashboard")
    public String financeDashboard(Model model, @RequestParam(required = false) String period) {
        // Get current date and calculate date ranges
        Date today = new Date();
        Date startDate, endDate = today;
        
        // Determine date range based on period parameter
        if ("daily".equals(period)) {
            startDate = today;
        } else if ("weekly".equals(period)) {
            startDate = new Date(today.getTime() - (7L * 24 * 60 * 60 * 1000));
        } else if ("monthly".equals(period)) {
            startDate = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        } else {
            // Default to 30 days
            startDate = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        }

        // Basic financial data
        Double totalRevenue = revenueService.getTotalRevenueByDateRange(startDate, endDate);
        Double totalExpenses = expenseService.getTotalExpensesByDateRange(startDate, endDate);
        Double totalInflows = cashFlowService.getTotalInflows(startDate, endDate);
        Double totalOutflows = cashFlowService.getTotalOutflows(startDate, endDate);
        Double netCashFlow = cashFlowService.getNetCashFlow(startDate, endDate);

        // Profit & Loss data
        Map<String, Object> profitLossReport = profitLossService.getProfitLossReport(startDate, endDate);

        // Revenue by source
        Map<String, Double> revenueBySource = new HashMap<>();
        revenueBySource.put("CATERING_ORDER", revenueService.getRevenueBySourceAndDateRange("CATERING_ORDER", startDate, endDate));
        revenueBySource.put("EVENT", revenueService.getRevenueBySourceAndDateRange("EVENT", startDate, endDate));
        revenueBySource.put("PACKAGE", revenueService.getRevenueBySourceAndDateRange("PACKAGE", startDate, endDate));
        revenueBySource.put("SUBSCRIPTION", revenueService.getRevenueBySourceAndDateRange("SUBSCRIPTION", startDate, endDate));

        // Expenses by category
        Map<String, Double> expensesByCategory = new HashMap<>();
        expensesByCategory.put("INGREDIENTS", expenseService.getExpensesByCategoryAndDateRange("INGREDIENTS", startDate, endDate));
        expensesByCategory.put("UTILITIES", expenseService.getExpensesByCategoryAndDateRange("UTILITIES", startDate, endDate));
        expensesByCategory.put("TRANSPORTATION", expenseService.getExpensesByCategoryAndDateRange("TRANSPORTATION", startDate, endDate));
        expensesByCategory.put("EQUIPMENT", expenseService.getExpensesByCategoryAndDateRange("EQUIPMENT", startDate, endDate));
        expensesByCategory.put("RENT", expenseService.getExpensesByCategoryAndDateRange("RENT", startDate, endDate));
        expensesByCategory.put("OTHER", expenseService.getExpensesByCategoryAndDateRange("OTHER", startDate, endDate));

        // Daily revenue trend (last 30 days)
        List<Map<String, Object>> dailyRevenueTrend = getDailyRevenueTrend(30);
        
        // Monthly revenue trend (last 12 months)
        List<Map<String, Object>> monthlyRevenueTrend = getMonthlyRevenueTrend(12);

        // Outstanding payments
        Long unpaidInvoices = invoiceService.getAllInvoices().stream()
                .filter(invoice -> "Unpaid".equals(invoice.getStatus()))
                .count();

        // Pending staff payments
        Long pendingStaffPayments = staffPaymentService.getAllStaffPayments().stream()
                .filter(payment -> "Pending".equals(payment.getStatus()))
                .count();

        // Add data to model
        model.addAttribute("period", period != null ? period : "monthly");
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalInflows", totalInflows);
        model.addAttribute("totalOutflows", totalOutflows);
        model.addAttribute("netCashFlow", netCashFlow);
        model.addAttribute("profitLossReport", profitLossReport);
        model.addAttribute("revenueBySource", revenueBySource);
        model.addAttribute("expensesByCategory", expensesByCategory);
        model.addAttribute("dailyRevenueTrend", dailyRevenueTrend);
        model.addAttribute("monthlyRevenueTrend", monthlyRevenueTrend);
        model.addAttribute("unpaidInvoices", unpaidInvoices);
        model.addAttribute("pendingStaffPayments", pendingStaffPayments);

        return "admin/finance-dashboard";
    }

    // Helper method to get daily revenue trend
    private List<Map<String, Object>> getDailyRevenueTrend(int days) {
        List<Map<String, Object>> trend = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (int i = days - 1; i >= 0; i--) {
            Date date = new Date(System.currentTimeMillis() - (i * 24L * 60 * 60 * 1000));
            Double revenue = revenueService.getTotalRevenueByDateRange(date, date);
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", sdf.format(date));
            dayData.put("revenue", revenue != null ? revenue : 0.0);
            trend.add(dayData);
        }
        return trend;
    }

    // Helper method to get monthly revenue trend
    private List<Map<String, Object>> getMonthlyRevenueTrend(int months) {
        List<Map<String, Object>> trend = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        
        for (int i = months - 1; i >= 0; i--) {
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -i);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            
            Double revenue = revenueService.getMonthlyRevenue(year, month);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", String.format("%04d-%02d", year, month));
            monthData.put("revenue", revenue != null ? revenue : 0.0);
            trend.add(monthData);
        }
        return trend;
    }

    // API endpoint for chart data
    @GetMapping("/admin/finance-dashboard/api/chart-data")
    @ResponseBody
    public Map<String, Object> getChartData(@RequestParam(required = false) String period) {
        Date today = new Date();
        Date startDate;
        
        if ("daily".equals(period)) {
            startDate = today;
        } else if ("weekly".equals(period)) {
            startDate = new Date(today.getTime() - (7L * 24 * 60 * 60 * 1000));
        } else {
            startDate = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        }

        Map<String, Object> chartData = new HashMap<>();
        
        // Revenue by source
        Map<String, Double> revenueBySource = new HashMap<>();
        revenueBySource.put("CATERING_ORDER", revenueService.getRevenueBySourceAndDateRange("CATERING_ORDER", startDate, today));
        revenueBySource.put("EVENT", revenueService.getRevenueBySourceAndDateRange("EVENT", startDate, today));
        revenueBySource.put("PACKAGE", revenueService.getRevenueBySourceAndDateRange("PACKAGE", startDate, today));
        revenueBySource.put("SUBSCRIPTION", revenueService.getRevenueBySourceAndDateRange("SUBSCRIPTION", startDate, today));
        
        // Expenses by category
        Map<String, Double> expensesByCategory = new HashMap<>();
        expensesByCategory.put("INGREDIENTS", expenseService.getExpensesByCategoryAndDateRange("INGREDIENTS", startDate, today));
        expensesByCategory.put("UTILITIES", expenseService.getExpensesByCategoryAndDateRange("UTILITIES", startDate, today));
        expensesByCategory.put("TRANSPORTATION", expenseService.getExpensesByCategoryAndDateRange("TRANSPORTATION", startDate, today));
        expensesByCategory.put("EQUIPMENT", expenseService.getExpensesByCategoryAndDateRange("EQUIPMENT", startDate, today));
        expensesByCategory.put("RENT", expenseService.getExpensesByCategoryAndDateRange("RENT", startDate, today));
        expensesByCategory.put("OTHER", expenseService.getExpensesByCategoryAndDateRange("OTHER", startDate, today));
        
        chartData.put("revenueBySource", revenueBySource);
        chartData.put("expensesByCategory", expensesByCategory);
        chartData.put("dailyTrend", getDailyRevenueTrend(30));
        chartData.put("monthlyTrend", getMonthlyRevenueTrend(12));
        
        return chartData;
    }

    // Report generation endpoints
    @GetMapping("/admin/finance-dashboard/reports/daily")
    public String generateDailyReport(Model model) {
        Date today = new Date();
        Map<String, Object> report = profitLossService.getProfitLossReport(today, today);
        model.addAttribute("report", report);
        model.addAttribute("reportType", "Daily");
        model.addAttribute("reportDate", new SimpleDateFormat("yyyy-MM-dd").format(today));
        return "admin/report-template";
    }

    @GetMapping("/admin/finance-dashboard/reports/weekly")
    public String generateWeeklyReport(Model model) {
        Date today = new Date();
        Date weekAgo = new Date(today.getTime() - (7L * 24 * 60 * 60 * 1000));
        Map<String, Object> report = profitLossService.getProfitLossReport(weekAgo, today);
        model.addAttribute("report", report);
        model.addAttribute("reportType", "Weekly");
        model.addAttribute("reportDate", new SimpleDateFormat("yyyy-MM-dd").format(weekAgo) + " to " + new SimpleDateFormat("yyyy-MM-dd").format(today));
        return "admin/report-template";
    }

    @GetMapping("/admin/finance-dashboard/reports/monthly")
    public String generateMonthlyReport(Model model) {
        Date today = new Date();
        Date monthAgo = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        Map<String, Object> report = profitLossService.getProfitLossReport(monthAgo, today);
        model.addAttribute("report", report);
        model.addAttribute("reportType", "Monthly");
        model.addAttribute("reportDate", new SimpleDateFormat("yyyy-MM-dd").format(monthAgo) + " to " + new SimpleDateFormat("yyyy-MM-dd").format(today));
        return "admin/report-template";
    }
}
