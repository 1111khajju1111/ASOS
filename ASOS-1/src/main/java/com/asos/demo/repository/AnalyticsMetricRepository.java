package com.asos.demo.repository;

import com.asos.demo.entity.AnalyticsMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalyticsMetricRepository extends JpaRepository<AnalyticsMetric, Long> {
    List<AnalyticsMetric> findByFounderIdOrderByPeriodDesc(Long founderId);
    List<AnalyticsMetric> findByFounderIdAndMetricNameOrderByPeriodDesc(Long founderId, String metricName);
}
