package com.asos.backend.repository;

import com.asos.backend.entity.FinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    List<FinanceRecord> findByFounderIdOrderByPeriodStartDesc(Long founderId);
}
