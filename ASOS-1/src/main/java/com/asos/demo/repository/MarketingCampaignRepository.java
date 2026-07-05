package com.asos.demo.repository;

import com.asos.demo.entity.MarketingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Long> {
    List<MarketingCampaign> findByFounderIdOrderByCreatedAtDesc(Long founderId);
}
