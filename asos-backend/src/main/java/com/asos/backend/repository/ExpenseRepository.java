package com.asos.backend.repository;

import com.asos.backend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByFounderIdOrderByExpenseDateDesc(Long founderId);
    List<Expense> findByFounderIdAndExpenseDateBetween(Long founderId, LocalDate start, LocalDate end);
}
