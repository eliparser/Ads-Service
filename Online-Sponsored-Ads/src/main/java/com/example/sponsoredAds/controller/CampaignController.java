package com.example.sponsoredAds.controller;

import com.example.sponsoredAds.models.Campaign;
import com.example.sponsoredAds.models.CampaignRequestDTO;
import com.example.sponsoredAds.service.CampaignService;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
public class CampaignController {
    private final CampaignService campaignService;
    private final Gson gson;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
        gson = new Gson();
    }

    @PostMapping("/campaign")
    public ResponseEntity<String> createCampaign(@RequestBody CampaignRequestDTO requestDTO) {
        Campaign createdCampaign = campaignService.createCampaign(requestDTO);

        if (createdCampaign != null) {
            log.info("campaign - {}", createdCampaign);
            String campaignJson = gson.toJson(createdCampaign);

            return ResponseEntity.accepted().body(campaignJson);
        } else {
            log.error("error creating campaign");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/campaign/{keyName}")
    public ResponseEntity<Object> getCampaign(@PathVariable String keyName) {
        Campaign campaign = campaignService.getCampaign(keyName);

        if (campaign != null) {
            log.info("Retrieved campaign - {}", campaign);
            return new ResponseEntity<>(campaign, HttpStatus.OK);
        } else {
            log.error("Campaign not found this key - {}", keyName);
            return new ResponseEntity<>("Campaign not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allCampaigns")
    public ResponseEntity<Object> getAllCampaigns() {
        List<Campaign> allCampaigns = campaignService.getAllCampaigns();

        if (!allCampaigns.isEmpty()) {
            log.info("Retrieved all campaigns - {}", allCampaigns);
            return new ResponseEntity<>(allCampaigns, HttpStatus.OK);
        } else {
            log.error("No campaigns found");
            return new ResponseEntity<>("No campaigns found", HttpStatus.NOT_FOUND);
        }
    }
}
