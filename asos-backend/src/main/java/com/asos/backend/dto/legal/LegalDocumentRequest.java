package com.asos.backend.dto.legal;

import com.asos.backend.entity.LegalDocStatus;
import com.asos.backend.entity.LegalDocType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LegalDocumentRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Type is required")
    private LegalDocType type;

    @NotNull(message = "Status is required")
    private LegalDocStatus status;

    private String content;

    private String riskNotes;

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
}
