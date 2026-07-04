package com.asos.backend.dto.marketing;

import com.asos.backend.entity.CampaignStatus;
import com.asos.backend.entity.CampaignType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MarketingCampaignResponse {

    private Long id;
    private String title;
    private CampaignType type;
    private CampaignStatus status;
    private String content;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;

    public MarketingCampaignResponse() {
    }

    public MarketingCampaignResponse(Long id, String title, CampaignType type, CampaignStatus status,
                                      String content, LocalDate scheduledDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.content = content;
        this.scheduledDate = scheduledDate;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CampaignType getType() {
        return type;
    }

    public void setType(CampaignType type) {
        this.type = type;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
