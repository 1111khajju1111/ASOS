package com.asos.demo.service;

import com.asos.demo.dto.analytics.AnalyticsMetricRequest;
import com.asos.demo.dto.analytics.AnalyticsMetricResponse;
import com.asos.demo.entity.AnalyticsMetric;
import com.asos.demo.entity.Founder;
import com.asos.demo.exception.ResourceNotFoundException;
import com.asos.demo.repository.AnalyticsMetricRepository;
import com.asos.demo.repository.FounderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final AnalyticsMetricRepository analyticsMetricRepository;
    private final FounderRepository founderRepository;

    public AnalyticsService(AnalyticsMetricRepository analyticsMetricRepository, FounderRepository founderRepository) {
        this.analyticsMetricRepository = analyticsMetricRepository;
        this.founderRepository = founderRepository;
    }

    public AnalyticsMetricResponse recordMetric(Long founderId, AnalyticsMetricRequest request) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        AnalyticsMetric metric = new AnalyticsMetric(founder, request.getMetricName(), request.getMetricValue(),
                request.getUnit(), request.getPeriod());

        return toResponse(analyticsMetricRepository.save(metric));
    }

    public List<AnalyticsMetricResponse> getMetrics(Long founderId) {
        return analyticsMetricRepository.findByFounderIdOrderByPeriodDesc(founderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AnalyticsMetricResponse> getMetricsByName(Long founderId, String metricName) {
        return analyticsMetricRepository.findByFounderIdAndMetricNameOrderByPeriodDesc(founderId, metricName).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AnalyticsMetricResponse toResponse(AnalyticsMetric m) {
        return new AnalyticsMetricResponse(m.getId(), m.getMetricName(), m.getMetricValue(), m.getUnit(),
                m.getPeriod(), m.getCreatedAt());
    }
}
