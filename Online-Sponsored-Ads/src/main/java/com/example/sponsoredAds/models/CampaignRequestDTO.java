package com.example.sponsoredAds.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class CampaignRequestDTO {
        private String name;
        private Date startDate;
        private List<String> products;
        private double bid;
}
