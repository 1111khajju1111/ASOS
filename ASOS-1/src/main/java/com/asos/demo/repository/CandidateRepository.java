package com.asos.demo.repository;

import com.asos.demo.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByJobPostingIdOrderByCreatedAtDesc(Long jobPostingId);
}
