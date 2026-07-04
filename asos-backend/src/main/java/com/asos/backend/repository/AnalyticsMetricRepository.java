package com.asos.backend.repository;

import com.asos.backend.entity.AnalyticsMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalyticsMetricRepository extends JpaRepository<AnalyticsMetric, Long> {
    List<AnalyticsMetric> findByFounderIdOrderByPeriodDesc(Long founderId);
    List<AnalyticsMetric> findByFounderIdAndMetricNameOrderByPeriodDesc(Long founderId, String metricName);
}
