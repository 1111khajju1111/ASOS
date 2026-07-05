package com.asos.demo.controller;

import com.asos.demo.dto.approval.ApprovalDecisionRequest;
import com.asos.demo.dto.approval.ApprovalRequestCreateRequest;
import com.asos.demo.dto.approval.ApprovalResponse;
import com.asos.demo.security.CurrentFounderId;
import com.asos.demo.service.ApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ResponseEntity<ApprovalResponse> create(@CurrentFounderId Long founderId,
                                                     @Valid @RequestBody ApprovalRequestCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(approvalService.createApprovalRequest(founderId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApprovalResponse>> getAll(@CurrentFounderId Long founderId,
                                                           @RequestParam(required = false) Boolean pendingOnly) {
        if (Boolean.TRUE.equals(pendingOnly)) {
            return ResponseEntity.ok(approvalService.getPendingApprovalRequests(founderId));
        }
        return ResponseEntity.ok(approvalService.getApprovalRequests(founderId));
    }

    @PatchMapping("/{approvalId}/decision")
    public ResponseEntity<ApprovalResponse> decide(@CurrentFounderId Long founderId,
                                                     @PathVariable Long approvalId,
                                                     @Valid @RequestBody ApprovalDecisionRequest request) {
        return ResponseEntity.ok(approvalService.decide(founderId, approvalId, request));
    }
}
