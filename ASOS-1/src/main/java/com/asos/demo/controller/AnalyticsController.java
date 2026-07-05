package com.asos.demo.controller;

import com.asos.demo.dto.analytics.AnalyticsMetricRequest;
import com.asos.demo.dto.analytics.AnalyticsMetricResponse;
import com.asos.demo.security.CurrentFounderId;
import com.asos.demo.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/metrics")
    public ResponseEntity<AnalyticsMetricResponse> recordMetric(@CurrentFounderId Long founderId,
                                                                  @Valid @RequestBody AnalyticsMetricRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(analyticsService.recordMetric(founderId, request));
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<AnalyticsMetricResponse>> getMetrics(
            @CurrentFounderId Long founderId,
            @RequestParam(required = false) String metricName) {

        if (metricName != null && !metricName.isBlank()) {
            return ResponseEntity.ok(analyticsService.getMetricsByName(founderId, metricName));
        }
        return ResponseEntity.ok(analyticsService.getMetrics(founderId));
    }
}
