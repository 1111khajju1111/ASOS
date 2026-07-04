package com.asos.backend.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinanceRecordResponse {

    private Long id;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal totalBudget;
    private BigDecimal totalExpenses;
    private BigDecimal cashBalance;
    private BigDecimal burnRate;
    private BigDecimal runwayMonths;
    private LocalDateTime createdAt;

    public FinanceRecordResponse() {
    }

    public FinanceRecordResponse(Long id, LocalDate periodStart, LocalDate periodEnd, BigDecimal totalBudget,
                                  BigDecimal totalExpenses, BigDecimal cashBalance, BigDecimal burnRate,
                                  BigDecimal runwayMonths, LocalDateTime createdAt) {
        this.id = id;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalBudget = totalBudget;
        this.totalExpenses = totalExpenses;
        this.cashBalance = cashBalance;
        this.burnRate = burnRate;
        this.runwayMonths = runwayMonths;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getBurnRate() {
        return burnRate;
    }

    public void setBurnRate(BigDecimal burnRate) {
        this.burnRate = burnRate;
    }

    public BigDecimal getRunwayMonths() {
        return runwayMonths;
    }

    public void setRunwayMonths(BigDecimal runwayMonths) {
        this.runwayMonths = runwayMonths;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
