package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.CashFlow;
import com.example.catering_system.admin.repository.CashFlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CashFlowService {

    @Autowired
    private CashFlowRepository cashFlowRepository;

    public CashFlow saveCashFlow(CashFlow cashFlow) {
        return cashFlowRepository.save(cashFlow);
    }

    public List<CashFlow> getAllCashFlows() {
        return cashFlowRepository.findAll();
    }

    public List<CashFlow> getCashFlowsByDateRange(Date startDate, Date endDate) {
        return cashFlowRepository.findByFlowDateBetween(startDate, endDate);
    }

    public List<CashFlow> getCashFlowsByType(String type) {
        return cashFlowRepository.findByType(type);
    }

    public List<CashFlow> getCashFlowsByCategory(String category) {
        return cashFlowRepository.findByCategory(category);
    }

    public Double getTotalInflows(Date startDate, Date endDate) {
        Double total = cashFlowRepository.getTotalCashFlowByTypeAndDateRange("INFLOW", startDate, endDate);
        return total != null ? total : 0.0;
    }

    public Double getTotalOutflows(Date startDate, Date endDate) {
        Double total = cashFlowRepository.getTotalCashFlowByTypeAndDateRange("OUTFLOW", startDate, endDate);
        return total != null ? total : 0.0;
    }

    public Double getNetCashFlow(Date startDate, Date endDate) {
        Double net = cashFlowRepository.getNetCashFlowByDateRange(startDate, endDate);
        return net != null ? net : 0.0;
    }

    // Liquidity tracking
    public Double getCurrentLiquidity() {
        Date today = new Date();
        Date thirtyDaysAgo = new Date(today.getTime() - (30L * 24 * 60 * 60 * 1000));
        return getNetCashFlow(thirtyDaysAgo, today);
    }

    // Delete methods
    public void deleteCashFlow(Long id) {
        cashFlowRepository.deleteById(id);
    }

    public void deleteAllCashFlows() {
        cashFlowRepository.deleteAll();
    }
}
