package com.asos.backend.dto.finance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseResponse {

    private Long id;
    private String category;
    private String description;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;

    public ExpenseResponse() {
    }

    public ExpenseResponse(Long id, String category, String description, BigDecimal amount,
                            LocalDate expenseDate, LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
