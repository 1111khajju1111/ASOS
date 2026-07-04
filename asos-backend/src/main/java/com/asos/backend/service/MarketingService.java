package com.asos.backend.service;

import com.asos.backend.dto.marketing.MarketingCampaignRequest;
import com.asos.backend.dto.marketing.MarketingCampaignResponse;
import com.asos.backend.entity.Founder;
import com.asos.backend.entity.MarketingCampaign;
import com.asos.backend.exception.ResourceNotFoundException;
import com.asos.backend.repository.FounderRepository;
import com.asos.backend.repository.MarketingCampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketingService {

    private final MarketingCampaignRepository marketingCampaignRepository;
    private final FounderRepository founderRepository;

    public MarketingService(MarketingCampaignRepository marketingCampaignRepository, FounderRepository founderRepository) {
        this.marketingCampaignRepository = marketingCampaignRepository;
        this.founderRepository = founderRepository;
    }

    public MarketingCampaignResponse createCampaign(Long founderId, MarketingCampaignRequest request) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        MarketingCampaign campaign = new MarketingCampaign(founder, request.getTitle(), request.getType(),
                request.getStatus(), request.getContent(), request.getScheduledDate());

        return toResponse(marketingCampaignRepository.save(campaign));
    }

    public List<MarketingCampaignResponse> getCampaigns(Long founderId) {
        return marketingCampaignRepository.findByFounderIdOrderByCreatedAtDesc(founderId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MarketingCampaignResponse updateCampaign(Long founderId, Long campaignId, MarketingCampaignRequest request) {
        MarketingCampaign campaign = getOwnedCampaign(founderId, campaignId);
        campaign.setTitle(request.getTitle());
        campaign.setType(request.getType());
        campaign.setStatus(request.getStatus());
        campaign.setContent(request.getContent());
        campaign.setScheduledDate(request.getScheduledDate());
        return toResponse(marketingCampaignRepository.save(campaign));
    }

    public void deleteCampaign(Long founderId, Long campaignId) {
        MarketingCampaign campaign = getOwnedCampaign(founderId, campaignId);
        marketingCampaignRepository.delete(campaign);
    }

    private MarketingCampaign getOwnedCampaign(Long founderId, Long campaignId) {
        MarketingCampaign campaign = marketingCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found"));

        if (!campaign.getFounder().getId().equals(founderId)) {
            throw new ResourceNotFoundException("Campaign not found");
        }

        return campaign;
    }

    private MarketingCampaignResponse toResponse(MarketingCampaign c) {
        return new MarketingCampaignResponse(c.getId(), c.getTitle(), c.getType(), c.getStatus(), c.getContent(),
                c.getScheduledDate(), c.getCreatedAt());
    }
}
