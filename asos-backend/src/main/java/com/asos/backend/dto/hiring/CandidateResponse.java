package com.asos.backend.dto.hiring;

import com.asos.backend.entity.CandidateStatus;
import java.time.LocalDateTime;

public class CandidateResponse {

    private Long id;
    private Long jobPostingId;
    private String name;
    private String email;
    private String resumeUrl;
    private CandidateStatus status;
    private String notes;
    private LocalDateTime createdAt;

    public CandidateResponse() {
    }

    public CandidateResponse(Long id, Long jobPostingId, String name, String email, String resumeUrl,
                              CandidateStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.jobPostingId = jobPostingId;
        this.name = name;
        this.email = email;
        this.resumeUrl = resumeUrl;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobPostingId() {
        return jobPostingId;
    }

    public void setJobPostingId(Long jobPostingId) {
        this.jobPostingId = jobPostingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public CandidateStatus getStatus() {
        return status;
    }

    public void setStatus(CandidateStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
