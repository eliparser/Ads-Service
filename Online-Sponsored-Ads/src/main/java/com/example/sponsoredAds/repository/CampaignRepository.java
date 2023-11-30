package com.example.sponsoredAds.repository;

import com.example.sponsoredAds.models.Campaign;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static com.example.sponsoredAds.utils.Utils.CAMP_ID_SUFFIX;

@Repository
@Log4j2
public class CampaignRepository {
    private final Gson gson;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public CampaignRepository(StringRedisTemplate stringRedisTemplate) {
        gson = new Gson();
        this.redisTemplate = stringRedisTemplate;
    }

    public void saveCampaign(Campaign campaign) {
        String campaignJson = serializationCampaign(campaign);

        log.info(" save in redis Campaign: {}", campaign.getKeyName());
        redisTemplate.opsForValue().set(campaign.getKeyName(), campaignJson);
    }

    public Campaign getCampaign(String keyName) {
        String campaignJson = redisTemplate.opsForValue().get(keyName);

        if (campaignJson != null) {
            log.info("got campaign: {}", keyName);
            return deserializationCampaign(campaignJson);
        } else {
            return null;
        }
    }

    public Set<String> getAllCampaignKeys() {
        return redisTemplate.keys(String.format("*%s*", CAMP_ID_SUFFIX));
    }

    private String serializationCampaign(Campaign campaign) {
        return gson.toJson(campaign);
    }

    private Campaign deserializationCampaign(String campaignJson) {
        return gson.fromJson(campaignJson, Campaign.class);
    }
}
