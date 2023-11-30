package com.example.sponsoredAds.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Product")
@Data
@Builder
@AllArgsConstructor
public class Product {

    @Id
    private String productName;
    private String title;
    private String category;
    private double price;
    private String serialNumber;
}
