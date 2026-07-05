package com.asos.demo.service;

import com.asos.demo.dto.legal.LegalDocumentRequest;
import com.asos.demo.dto.legal.LegalDocumentResponse;
import com.asos.demo.entity.Founder;
import com.asos.demo.entity.LegalDocument;
import com.asos.demo.exception.ResourceNotFoundException;
import com.asos.demo.repository.FounderRepository;
import com.asos.demo.repository.LegalDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LegalService {

    private final LegalDocumentRepository legalDocumentRepository;
    private final FounderRepository founderRepository;

    public LegalService(LegalDocumentRepository legalDocumentRepository, FounderRepository founderRepository) {
        this.legalDocumentRepository = legalDocumentRepository;
        this.founderRepository = founderRepository;
    }

    public LegalDocumentResponse createDocument(Long founderId, LegalDocumentRequest request) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        LegalDocument document = new LegalDocument(founder, request.getTitle(), request.getType(),
                request.getStatus(), request.getContent());
        document.setRiskNotes(request.getRiskNotes());

        return toResponse(legalDocumentRepository.save(document));
    }

    public List<LegalDocumentResponse> getDocuments(Long founderId) {
        return legalDocumentRepository.findByFounderIdOrderByCreatedAtDesc(founderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public LegalDocumentResponse updateDocument(Long founderId, Long documentId, LegalDocumentRequest request) {
        LegalDocument document = getOwnedDocument(founderId, documentId);
        document.setTitle(request.getTitle());
        document.setType(request.getType());
        document.setStatus(request.getStatus());
        document.setContent(request.getContent());
        document.setRiskNotes(request.getRiskNotes());
        return toResponse(legalDocumentRepository.save(document));
    }

    public void deleteDocument(Long founderId, Long documentId) {
        LegalDocument document = getOwnedDocument(founderId, documentId);
        legalDocumentRepository.delete(document);
    }

    private LegalDocument getOwnedDocument(Long founderId, Long documentId) {
        LegalDocument document = legalDocumentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Legal document not found"));

        if (!document.getFounder().getId().equals(founderId)) {
            throw new ResourceNotFoundException("Legal document not found");
        }

        return document;
    }

    private LegalDocumentResponse toResponse(LegalDocument d) {
        return new LegalDocumentResponse(d.getId(), d.getTitle(), d.getType(), d.getStatus(), d.getContent(),
                d.getRiskNotes(), d.getCreatedAt());
    }
}
