package com.asos.backend.dto.legal;

import com.asos.backend.entity.LegalDocStatus;
import com.asos.backend.entity.LegalDocType;
import java.time.LocalDateTime;

public class LegalDocumentResponse {

    private Long id;
    private String title;
    private LegalDocType type;
    private LegalDocStatus status;
    private String content;
    private String riskNotes;
    private LocalDateTime createdAt;

    public LegalDocumentResponse() {
    }

    public LegalDocumentResponse(Long id, String title, LegalDocType type, LegalDocStatus status,
                                  String content, String riskNotes, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.content = content;
        this.riskNotes = riskNotes;
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

    public LegalDocType getType() {
        return type;
    }

    public void setType(LegalDocType type) {
        this.type = type;
    }

    public LegalDocStatus getStatus() {
        return status;
    }

    public void setStatus(LegalDocStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRiskNotes() {
        return riskNotes;
    }

    public void setRiskNotes(String riskNotes) {
        this.riskNotes = riskNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
