package com.asos.demo.service;

import com.asos.demo.dto.hiring.CandidateRequest;
import com.asos.demo.dto.hiring.CandidateResponse;
import com.asos.demo.dto.hiring.JobPostingRequest;
import com.asos.demo.dto.hiring.JobPostingResponse;
import com.asos.demo.entity.Candidate;
import com.asos.demo.entity.Founder;
import com.asos.demo.entity.JobPosting;
import com.asos.demo.exception.ResourceNotFoundException;
import com.asos.demo.repository.CandidateRepository;
import com.asos.demo.repository.FounderRepository;
import com.asos.demo.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HiringService {

    private final JobPostingRepository jobPostingRepository;
    private final CandidateRepository candidateRepository;
    private final FounderRepository founderRepository;

    public HiringService(JobPostingRepository jobPostingRepository, CandidateRepository candidateRepository,
                          FounderRepository founderRepository) {
        this.jobPostingRepository = jobPostingRepository;
        this.candidateRepository = candidateRepository;
        this.founderRepository = founderRepository;
    }

    public JobPostingResponse createJobPosting(Long founderId, JobPostingRequest request) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        JobPosting posting = new JobPosting(founder, request.getTitle(), request.getDescription(),
                request.getDepartment(), request.getStatus());

        JobPosting saved = jobPostingRepository.save(posting);
        return toJobPostingResponse(saved);
    }

    public List<JobPostingResponse> getJobPostings(Long founderId) {
        return jobPostingRepository.findByFounderIdOrderByCreatedAtDesc(founderId).stream()
                .map(this::toJobPostingResponse)
                .collect(Collectors.toList());
    }

    public JobPostingResponse updateJobPosting(Long founderId, Long jobPostingId, JobPostingRequest request) {
        JobPosting posting = getOwnedJobPosting(founderId, jobPostingId);
        posting.setTitle(request.getTitle());
        posting.setDescription(request.getDescription());
        posting.setDepartment(request.getDepartment());
        posting.setStatus(request.getStatus());
        return toJobPostingResponse(jobPostingRepository.save(posting));
    }

    public void deleteJobPosting(Long founderId, Long jobPostingId) {
        JobPosting posting = getOwnedJobPosting(founderId, jobPostingId);
        jobPostingRepository.delete(posting);
    }

    public CandidateResponse addCandidate(Long founderId, Long jobPostingId, CandidateRequest request) {
        JobPosting posting = getOwnedJobPosting(founderId, jobPostingId);

        Candidate candidate = new Candidate(posting, request.getName(), request.getEmail(),
                request.getResumeUrl(), request.getStatus());
        candidate.setNotes(request.getNotes());

        Candidate saved = candidateRepository.save(candidate);
        return toCandidateResponse(saved);
    }

    public List<CandidateResponse> getCandidates(Long founderId, Long jobPostingId) {
        getOwnedJobPosting(founderId, jobPostingId); // ownership check
        return candidateRepository.findByJobPostingIdOrderByCreatedAtDesc(jobPostingId).stream()
                .map(this::toCandidateResponse)
                .collect(Collectors.toList());
    }

    public CandidateResponse updateCandidate(Long founderId, Long jobPostingId, Long candidateId, CandidateRequest request) {
        getOwnedJobPosting(founderId, jobPostingId); // ownership check
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        if (!candidate.getJobPosting().getId().equals(jobPostingId)) {
            throw new ResourceNotFoundException("Candidate not found for this job posting");
        }

        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());
        candidate.setResumeUrl(request.getResumeUrl());
        candidate.setStatus(request.getStatus());
        candidate.setNotes(request.getNotes());

        return toCandidateResponse(candidateRepository.save(candidate));
    }

    private JobPosting getOwnedJobPosting(Long founderId, Long jobPostingId) {
        JobPosting posting = jobPostingRepository.findById(jobPostingId)
                .orElseThrow(() -> new ResourceNotFoundException("Job posting not found"));

        if (!posting.getFounder().getId().equals(founderId)) {
            throw new ResourceNotFoundException("Job posting not found");
        }

        return posting;
    }

    private JobPostingResponse toJobPostingResponse(JobPosting p) {
        return new JobPostingResponse(p.getId(), p.getTitle(), p.getDescription(), p.getDepartment(),
                p.getStatus(), p.getCandidates() != null ? p.getCandidates().size() : 0, p.getCreatedAt());
    }

    private CandidateResponse toCandidateResponse(Candidate c) {
        return new CandidateResponse(c.getId(), c.getJobPosting().getId(), c.getName(), c.getEmail(),
                c.getResumeUrl(), c.getStatus(), c.getNotes(), c.getCreatedAt());
    }
}
