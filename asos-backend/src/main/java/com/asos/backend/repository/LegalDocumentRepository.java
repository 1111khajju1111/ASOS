package com.asos.backend.repository;

import com.asos.backend.entity.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
    List<LegalDocument> findByFounderIdOrderByCreatedAtDesc(Long founderId);
}
