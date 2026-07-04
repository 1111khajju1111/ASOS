package com.asos.backend.service;

import com.asos.backend.dto.approval.ApprovalDecisionRequest;
import com.asos.backend.dto.approval.ApprovalRequestCreateRequest;
import com.asos.backend.dto.approval.ApprovalResponse;
import com.asos.backend.entity.ApprovalRequest;
import com.asos.backend.entity.ApprovalStatus;
import com.asos.backend.entity.Founder;
import com.asos.backend.exception.ResourceNotFoundException;
import com.asos.backend.repository.ApprovalRequestRepository;
import com.asos.backend.repository.FounderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final FounderRepository founderRepository;

    public ApprovalService(ApprovalRequestRepository approvalRequestRepository, FounderRepository founderRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.founderRepository = founderRepository;
    }

    public ApprovalResponse createApprovalRequest(Long founderId, ApprovalRequestCreateRequest request) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        ApprovalRequest approvalRequest = new ApprovalRequest(founder, request.getModule(), request.getTitle(),
                request.getDescription(), request.getRelatedEntityId());

        return toResponse(approvalRequestRepository.save(approvalRequest));
    }

    public List<ApprovalResponse> getApprovalRequests(Long founderId) {
        return approvalRequestRepository.findByFounderIdOrderByRequestedAtDesc(founderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ApprovalResponse> getPendingApprovalRequests(Long founderId) {
        return approvalRequestRepository.findByFounderIdAndStatusOrderByRequestedAtDesc(founderId, ApprovalStatus.PENDING).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ApprovalResponse decide(Long founderId, Long approvalId, ApprovalDecisionRequest request) {
        if (request.getStatus() == ApprovalStatus.PENDING) {
            throw new IllegalArgumentException("Decision must be APPROVED or REJECTED");
        }

        ApprovalRequest approvalRequest = approvalRequestRepository.findById(approvalId)
                .orElseThrow(() -> new ResourceNotFoundException("Approval request not found"));

        if (!approvalRequest.getFounder().getId().equals(founderId)) {
            throw new ResourceNotFoundException("Approval request not found");
        }

        approvalRequest.setStatus(request.getStatus());
        approvalRequest.setResolvedAt(LocalDateTime.now());

        return toResponse(approvalRequestRepository.save(approvalRequest));
    }

    private ApprovalResponse toResponse(ApprovalRequest a) {
        return new ApprovalResponse(a.getId(), a.getModule(), a.getTitle(), a.getDescription(),
                a.getRelatedEntityId(), a.getStatus(), a.getRequestedAt(), a.getResolvedAt());
    }
}
