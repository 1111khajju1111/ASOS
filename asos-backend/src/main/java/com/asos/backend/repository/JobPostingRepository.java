package com.asos.backend.repository;

import com.asos.backend.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByFounderIdOrderByCreatedAtDesc(Long founderId);
}
