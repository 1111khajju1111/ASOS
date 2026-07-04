package com.asos.backend.dto.analytics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AnalyticsMetricResponse {

    private Long id;
    private String metricName;
    private BigDecimal metricValue;
    private String unit;
    private LocalDate period;
    private LocalDateTime createdAt;

    public AnalyticsMetricResponse() {
    }

    public AnalyticsMetricResponse(Long id, String metricName, BigDecimal metricValue, String unit,
                                    LocalDate period, LocalDateTime createdAt) {
        this.id = id;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.unit = unit;
        this.period = period;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public BigDecimal getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(BigDecimal metricValue) {
        this.metricValue = metricValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
