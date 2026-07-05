package com.asos.demo.dto.hiring;

import com.asos.demo.entity.JobStatus;
import java.time.LocalDateTime;
import java.util.List;

public class JobPostingResponse {

    private Long id;
    private String title;
    private String description;
    private String department;
    private JobStatus status;
    private int candidateCount;
    private LocalDateTime createdAt;

    public JobPostingResponse() {
    }

    public JobPostingResponse(Long id, String title, String description, String department,
                               JobStatus status, int candidateCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.department = department;
        this.status = status;
        this.candidateCount = candidateCount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public int getCandidateCount() {
        return candidateCount;
    }

    public void setCandidateCount(int candidateCount) {
        this.candidateCount = candidateCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
