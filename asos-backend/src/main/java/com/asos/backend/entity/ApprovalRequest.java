package com.asos.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_requests")
public class ApprovalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "founder_id", nullable = false)
    private Founder founder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalModule module;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Optional pointer to the related record (e.g. expense id, campaign id)
    private Long relatedEntityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime resolvedAt;

    public ApprovalRequest() {
    }

    public ApprovalRequest(Founder founder, ApprovalModule module, String title, String description, Long relatedEntityId) {
        this.founder = founder;
        this.module = module;
        this.title = title;
        this.description = description;
        this.relatedEntityId = relatedEntityId;
        this.status = ApprovalStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        this.requestedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Founder getFounder() {
        return founder;
    }

    public void setFounder(Founder founder) {
        this.founder = founder;
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
