package com.asos.demo.controller;

import com.asos.demo.dto.legal.LegalDocumentRequest;
import com.asos.demo.dto.legal.LegalDocumentResponse;
import com.asos.demo.security.CurrentFounderId;
import com.asos.demo.service.LegalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legal")
public class LegalController {

    private final LegalService legalService;

    public LegalController(LegalService legalService) {
        this.legalService = legalService;
    }

    @PostMapping("/documents")
    public ResponseEntity<LegalDocumentResponse> createDocument(@CurrentFounderId Long founderId,
                                                                  @Valid @RequestBody LegalDocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(legalService.createDocument(founderId, request));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<LegalDocumentResponse>> getDocuments(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(legalService.getDocuments(founderId));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<LegalDocumentResponse> updateDocument(@CurrentFounderId Long founderId,
                                                                  @PathVariable Long documentId,
                                                                  @Valid @RequestBody LegalDocumentRequest request) {
        return ResponseEntity.ok(legalService.updateDocument(founderId, documentId, request));
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(@CurrentFounderId Long founderId, @PathVariable Long documentId) {
        legalService.deleteDocument(founderId, documentId);
        return ResponseEntity.noContent().build();
    }
}
