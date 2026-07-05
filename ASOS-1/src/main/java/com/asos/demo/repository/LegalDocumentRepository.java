package com.asos.demo.repository;

import com.asos.demo.entity.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
    List<LegalDocument> findByFounderIdOrderByCreatedAtDesc(Long founderId);
}
