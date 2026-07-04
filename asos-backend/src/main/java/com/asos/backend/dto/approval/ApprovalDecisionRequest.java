package com.asos.backend.dto.approval;

import com.asos.backend.entity.ApprovalStatus;
import jakarta.validation.constraints.NotNull;

public class ApprovalDecisionRequest {

    @NotNull(message = "Decision status is required (APPROVED or REJECTED)")
    private ApprovalStatus status;

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }
}
