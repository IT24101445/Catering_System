package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.Revenue;
import com.example.catering_system.admin.repository.RevenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    public Revenue saveRevenue(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    public List<Revenue> getAllRevenues() {
        return revenueRepository.findAll();
    }

    public List<Revenue> getRevenuesByDateRange(Date startDate, Date endDate) {
        return revenueRepository.findByRevenueDateBetween(startDate, endDate);
    }

    public List<Revenue> getRevenuesBySource(String source) {
        return revenueRepository.findBySource(source);
    }

    public List<Revenue> getRevenuesByStatus(String status) {
        return revenueRepository.findByStatus(status);
    }

    public Double getTotalRevenueByDateRange(Date startDate, Date endDate) {
        Double total = revenueRepository.getTotalRevenueByDateRange(startDate, endDate);
        return total != null ? total : 0.0;
    }

    public Double getRevenueBySourceAndDateRange(String source, Date startDate, Date endDate) {
        Double total = revenueRepository.getRevenueBySourceAndDateRange(source, startDate, endDate);
        return total != null ? total : 0.0;
    }

    // Daily, Monthly, Annual summaries
    public Double getDailyRevenue(Date date) {
        return getTotalRevenueByDateRange(date, date);
    }

    public Double getMonthlyRevenue(int year, int month) {
        Date startDate = new Date(year - 1900, month - 1, 1);
        Date endDate = new Date(year - 1900, month, 0);
        return getTotalRevenueByDateRange(startDate, endDate);
    }

    public Double getAnnualRevenue(int year) {
        Date startDate = new Date(year - 1900, 0, 1);
        Date endDate = new Date(year - 1900, 11, 31);
        return getTotalRevenueByDateRange(startDate, endDate);
    }

    // Delete methods
    public void deleteRevenue(Long id) {
        revenueRepository.deleteById(id);
    }

    public void deleteAllRevenues() {
        revenueRepository.deleteAll();
    }
}
