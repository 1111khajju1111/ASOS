package com.asos.demo.repository;

import com.asos.demo.entity.ApprovalRequest;
import com.asos.demo.entity.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {
    List<ApprovalRequest> findByFounderIdOrderByRequestedAtDesc(Long founderId);
    List<ApprovalRequest> findByFounderIdAndStatusOrderByRequestedAtDesc(Long founderId, ApprovalStatus status);
}
