package com.asos.demo.dto.approval;

import com.asos.demo.entity.ApprovalModule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ApprovalRequestCreateRequest {

    @NotNull(message = "Module is required")
    private ApprovalModule module;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Long relatedEntityId;

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
}
