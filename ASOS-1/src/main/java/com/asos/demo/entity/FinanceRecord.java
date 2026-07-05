package com.asos.demo.entity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "finance_records")
public class FinanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "founder_id", nullable = false)
    private Founder founder;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    @Column(nullable = false)
    private BigDecimal totalBudget;

    @Column(nullable = false)
    private BigDecimal totalExpenses;

    @Column(nullable = false)
    private BigDecimal cashBalance;

    // Derived/calculated fields (computed in service layer)
    private BigDecimal burnRate;
    private BigDecimal runwayMonths;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public FinanceRecord() {
    }

    public FinanceRecord(Founder founder, LocalDate periodStart, LocalDate periodEnd,
                          BigDecimal totalBudget, BigDecimal totalExpenses, BigDecimal cashBalance) {
        this.founder = founder;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalBudget = totalBudget;
        this.totalExpenses = totalExpenses;
        this.cashBalance = cashBalance;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Founder getFounder() {
        return founder;
    }

    public void setFounder(Founder founder) {
        this.founder = founder;
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
