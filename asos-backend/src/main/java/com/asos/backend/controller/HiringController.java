package com.asos.backend.controller;

import com.asos.backend.dto.hiring.CandidateRequest;
import com.asos.backend.dto.hiring.CandidateResponse;
import com.asos.backend.dto.hiring.JobPostingRequest;
import com.asos.backend.dto.hiring.JobPostingResponse;
import com.asos.backend.security.CurrentFounderId;
import com.asos.backend.service.HiringService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hiring")
public class HiringController {

    private final HiringService hiringService;

    public HiringController(HiringService hiringService) {
        this.hiringService = hiringService;
    }

    @PostMapping("/jobs")
    public ResponseEntity<JobPostingResponse> createJob(@CurrentFounderId Long founderId,
                                                          @Valid @RequestBody JobPostingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hiringService.createJobPosting(founderId, request));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobPostingResponse>> getJobs(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(hiringService.getJobPostings(founderId));
    }

    @PutMapping("/jobs/{jobPostingId}")
    public ResponseEntity<JobPostingResponse> updateJob(@CurrentFounderId Long founderId,
                                                          @PathVariable Long jobPostingId,
                                                          @Valid @RequestBody JobPostingRequest request) {
        return ResponseEntity.ok(hiringService.updateJobPosting(founderId, jobPostingId, request));
    }

    @DeleteMapping("/jobs/{jobPostingId}")
    public ResponseEntity<Void> deleteJob(@CurrentFounderId Long founderId, @PathVariable Long jobPostingId) {
        hiringService.deleteJobPosting(founderId, jobPostingId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jobs/{jobPostingId}/candidates")
    public ResponseEntity<CandidateResponse> addCandidate(@CurrentFounderId Long founderId,
                                                            @PathVariable Long jobPostingId,
                                                            @Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hiringService.addCandidate(founderId, jobPostingId, request));
    }

    @GetMapping("/jobs/{jobPostingId}/candidates")
    public ResponseEntity<List<CandidateResponse>> getCandidates(@CurrentFounderId Long founderId,
                                                                   @PathVariable Long jobPostingId) {
        return ResponseEntity.ok(hiringService.getCandidates(founderId, jobPostingId));
    }

    @PutMapping("/jobs/{jobPostingId}/candidates/{candidateId}")
    public ResponseEntity<CandidateResponse> updateCandidate(@CurrentFounderId Long founderId,
                                                               @PathVariable Long jobPostingId,
                                                               @PathVariable Long candidateId,
                                                               @Valid @RequestBody CandidateRequest request) {
        return ResponseEntity.ok(hiringService.updateCandidate(founderId, jobPostingId, candidateId, request));
    }
}
