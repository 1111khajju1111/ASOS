package com.asos.backend.controller;

import com.asos.backend.dto.marketing.MarketingCampaignRequest;
import com.asos.backend.dto.marketing.MarketingCampaignResponse;
import com.asos.backend.security.CurrentFounderId;
import com.asos.backend.service.MarketingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketing")
public class MarketingController {

    private final MarketingService marketingService;

    public MarketingController(MarketingService marketingService) {
        this.marketingService = marketingService;
    }

    @PostMapping("/campaigns")
    public ResponseEntity<MarketingCampaignResponse> createCampaign(@CurrentFounderId Long founderId,
                                                                      @Valid @RequestBody MarketingCampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketingService.createCampaign(founderId, request));
    }

    @GetMapping("/campaigns")
    public ResponseEntity<List<MarketingCampaignResponse>> getCampaigns(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(marketingService.getCampaigns(founderId));
    }

    @PutMapping("/campaigns/{campaignId}")
    public ResponseEntity<MarketingCampaignResponse> updateCampaign(@CurrentFounderId Long founderId,
                                                                      @PathVariable Long campaignId,
                                                                      @Valid @RequestBody MarketingCampaignRequest request) {
        return ResponseEntity.ok(marketingService.updateCampaign(founderId, campaignId, request));
    }

    @DeleteMapping("/campaigns/{campaignId}")
    public ResponseEntity<Void> deleteCampaign(@CurrentFounderId Long founderId, @PathVariable Long campaignId) {
        marketingService.deleteCampaign(founderId, campaignId);
        return ResponseEntity.noContent().build();
    }
}
