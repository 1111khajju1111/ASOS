package com.asos.backend.controller;

import com.asos.backend.dto.finance.ExpenseRequest;
import com.asos.backend.dto.finance.ExpenseResponse;
import com.asos.backend.dto.finance.FinanceRecordRequest;
import com.asos.backend.dto.finance.FinanceRecordResponse;
import com.asos.backend.security.CurrentFounderId;
import com.asos.backend.service.FinanceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping("/records")
    public ResponseEntity<FinanceRecordResponse> createRecord(@CurrentFounderId Long founderId,
                                                                @Valid @RequestBody FinanceRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createFinanceRecord(founderId, request));
    }

    @GetMapping("/records")
    public ResponseEntity<List<FinanceRecordResponse>> getRecords(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(financeService.getFinanceRecords(founderId));
    }

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseResponse> addExpense(@CurrentFounderId Long founderId,
                                                        @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.addExpense(founderId, request));
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponse>> getExpenses(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(financeService.getExpenses(founderId));
    }

    @DeleteMapping("/expenses/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@CurrentFounderId Long founderId, @PathVariable Long expenseId) {
        financeService.deleteExpense(founderId, expenseId);
        return ResponseEntity.noContent().build();
    }
}
