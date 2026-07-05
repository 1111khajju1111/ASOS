package com.asos.demo.repository;

import com.asos.demo.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    List<JobPosting> findByFounderIdOrderByCreatedAtDesc(Long founderId);
}
