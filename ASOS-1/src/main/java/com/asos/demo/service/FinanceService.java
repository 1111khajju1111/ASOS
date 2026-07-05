package com.asos.demo.service;

import com.asos.demo.dto.finance.ExpenseRequest;
import com.asos.demo.dto.finance.ExpenseResponse;
import com.asos.demo.dto.finance.FinanceRecordRequest;
import com.asos.demo.dto.finance.FinanceRecordResponse;
import com.asos.demo.entity.Expense;
import com.asos.demo.entity.FinanceRecord;
import com.asos.demo.entity.Founder;
import com.asos.demo.exception.ResourceNotFoundException;
import com.asos.demo.repository.ExpenseRepository;
import com.asos.demo.repository.FinanceRecordRepository;
import com.asos.demo.repository.FounderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceService {

    private final FinanceRecordRepository financeRecordRepository;
    private final ExpenseRepository expenseRepository;
    private final FounderRepository founderRepository;

    public FinanceService(FinanceRecordRepository financeRecordRepository, ExpenseRepository expenseRepository,
                           FounderRepository founderRepository) {
        this.financeRecordRepository = financeRecordRepository;
        this.expenseRepository = expenseRepository;
        this.founderRepository = founderRepository;
    }

    public FinanceRecordResponse createFinanceRecord(Long founderId, FinanceRecordRequest request) {
        Founder founder = getFounder(founderId);

        FinanceRecord record = new FinanceRecord(founder, request.getPeriodStart(), request.getPeriodEnd(),
                request.getTotalBudget(), request.getTotalExpenses(), request.getCashBalance());

        // Burn rate = average monthly spend over the period
        long months = Math.max(1, ChronoUnit.MONTHS.between(request.getPeriodStart(), request.getPeriodEnd()));
        BigDecimal burnRate = request.getTotalExpenses()
                .divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        record.setBurnRate(burnRate);

        // Runway (months) = cash balance / monthly burn rate
        BigDecimal runwayMonths = burnRate.compareTo(BigDecimal.ZERO) > 0
                ? request.getCashBalance().divide(burnRate, 1, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(-1); // -1 signals "infinite/undefined" (no burn)
        record.setRunwayMonths(runwayMonths);

        FinanceRecord saved = financeRecordRepository.save(record);
        return toFinanceRecordResponse(saved);
    }

    public List<FinanceRecordResponse> getFinanceRecords(Long founderId) {
        return financeRecordRepository.findByFounderIdOrderByPeriodStartDesc(founderId).stream()
                .map(this::toFinanceRecordResponse)
                .collect(Collectors.toList());
    }

    public ExpenseResponse addExpense(Long founderId, ExpenseRequest request) {
        Founder founder = getFounder(founderId);
        Expense expense = new Expense(founder, request.getCategory(), request.getDescription(),
                request.getAmount(), request.getExpenseDate());
        Expense saved = expenseRepository.save(expense);
        return toExpenseResponse(saved);
    }

    public List<ExpenseResponse> getExpenses(Long founderId) {
        return expenseRepository.findByFounderIdOrderByExpenseDateDesc(founderId).stream()
                .map(this::toExpenseResponse)
                .collect(Collectors.toList());
    }

    public void deleteExpense(Long founderId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!expense.getFounder().getId().equals(founderId)) {
            throw new ResourceNotFoundException("Expense not found");
        }

        expenseRepository.delete(expense);
    }

    private Founder getFounder(Long founderId) {
        return founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));
    }

    private FinanceRecordResponse toFinanceRecordResponse(FinanceRecord r) {
        return new FinanceRecordResponse(r.getId(), r.getPeriodStart(), r.getPeriodEnd(), r.getTotalBudget(),
                r.getTotalExpenses(), r.getCashBalance(), r.getBurnRate(), r.getRunwayMonths(), r.getCreatedAt());
    }

    private ExpenseResponse toExpenseResponse(Expense e) {
        return new ExpenseResponse(e.getId(), e.getCategory(), e.getDescription(), e.getAmount(),
                e.getExpenseDate(), e.getCreatedAt());
    }
}
