package com.example.sponsoredAds.service;

import com.example.sponsoredAds.models.Campaign;
import com.example.sponsoredAds.models.CampaignRequestDTO;
import com.example.sponsoredAds.models.Product;
import com.example.sponsoredAds.repository.CampaignRepository;
import com.example.sponsoredAds.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.sponsoredAds.utils.Utils.CAMP_ID_SUFFIX;
import static com.example.sponsoredAds.utils.Utils.PROD_SUFFIX;

@Service
@AllArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;
    private final ProductRepository productRepository;

    public Campaign createCampaign(CampaignRequestDTO requestDTO) {
        Campaign campaign = Campaign.builder().build();
        campaign.setKeyName(String.format("%s-%s", requestDTO.getName(), CAMP_ID_SUFFIX));
        campaign.setName(requestDTO.getName());
        campaign.setStartDate(requestDTO.getStartDate());
        campaign.setBid(requestDTO.getBid());
        campaign.setProducts(requestDTO.getProducts());

        //Save in redis
        campaignRepository.saveCampaign(campaign);

        return campaign;
    }

    public Product getPromotedProductByCategory(String category) {
        List<Campaign> activeCampaigns = getAllCampaigns().stream().filter(this::isActiveCampaign).toList();
        Set<String> productsByCategory = productRepository.getProductsByCategory(category);

        double maxBid = Double.MIN_VALUE;
        Product selectedProduct = null;

        for (Campaign campaign : activeCampaigns) {
            List<String> productsWithSuffix = addSuffix(campaign.getProducts(), category);

            for (String productKey : productsWithSuffix) {
                if (productsByCategory.contains(productKey)) {
                    Product product = productRepository.getProduct(productKey);
                    if (product != null && campaign.getBid() > maxBid) {
                        maxBid = campaign.getBid();
                        selectedProduct = product;
                    }
                }
            }
        }

        //In case there are no promoted product for the matching category
        if (selectedProduct == null) {
            String prodId = getProdId(activeCampaigns);
            selectedProduct = productRepository.getProduct(prodId);
        }
        return selectedProduct;
    }

    public Campaign getCampaign(String keyName) {
        return campaignRepository.getCampaign(keyName);
    }

    public List<Campaign> getAllCampaigns() {
        Set<String> campaignKeys = campaignRepository.getAllCampaignKeys();
        List<Campaign> campaigns = new ArrayList<>();

        if (campaignKeys != null) {
            campaigns = campaignKeys.stream()
                    .map(this::getCampaign)
                    .filter(Objects::nonNull)
                    .toList();
        }
        return campaigns;
    }

    private static String getProdId(List<Campaign> activeCampaigns) {
        return activeCampaigns.stream()
                .max(Comparator.comparingDouble(Campaign::getBid))
                .orElseThrow().getProducts().get(0);
    }

    private List<String> addSuffix(List<String> products, String category) {
        return products.stream()
                .map(product -> String.format("%s-%s-%s", product, category, PROD_SUFFIX))
                .toList();
    }

    private boolean isActiveCampaign(Campaign campaign) {
        return campaign != null && campaign.isActiveCampaign() && campaign.getProducts() != null;
    }
}
