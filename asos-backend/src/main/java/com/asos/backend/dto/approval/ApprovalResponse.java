package com.asos.backend.dto.approval;

import com.asos.backend.entity.ApprovalModule;
import com.asos.backend.entity.ApprovalStatus;
import java.time.LocalDateTime;

public class ApprovalResponse {

    private Long id;
    private ApprovalModule module;
    private String title;
    private String description;
    private Long relatedEntityId;
    private ApprovalStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;

    public ApprovalResponse() {
    }

    public ApprovalResponse(Long id, ApprovalModule module, String title, String description, Long relatedEntityId,
                             ApprovalStatus status, LocalDateTime requestedAt, LocalDateTime resolvedAt) {
        this.id = id;
        this.module = module;
        this.title = title;
        this.description = description;
        this.relatedEntityId = relatedEntityId;
        this.status = status;
        this.requestedAt = requestedAt;
        this.resolvedAt = resolvedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalModule getModule() {
        return module;
    }

    public void setModule(ApprovalModule module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Long relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
