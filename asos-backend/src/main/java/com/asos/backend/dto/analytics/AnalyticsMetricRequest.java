package com.asos.backend.dto.analytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnalyticsMetricRequest {

    @NotBlank(message = "Metric name is required")
    private String metricName;

    @NotNull(message = "Metric value is required")
    private BigDecimal metricValue;

    private String unit;

    @NotNull(message = "Period is required")
    private LocalDate period;

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
}
