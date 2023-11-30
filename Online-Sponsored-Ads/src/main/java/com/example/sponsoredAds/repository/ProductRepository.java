package com.example.sponsoredAds.repository;

import com.example.sponsoredAds.models.Product;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static com.example.sponsoredAds.utils.Utils.PROD_SUFFIX;

@Repository
@Log4j2
public class ProductRepository {
    private final StringRedisTemplate redisTemplate;
    private final Gson gson;

    @Autowired
    public ProductRepository(StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = stringRedisTemplate;
        gson = new Gson();
    }

    public void saveProduct(Product product) {
        String productJson = serializationProduct(product);

        log.info("save on redis. product: {}", product.getProductName());
        redisTemplate.opsForValue().set(product.getProductName(), productJson);
    }

    public Product getProduct(String name) {
        String productJson = redisTemplate.opsForValue().get(name);

        if (productJson != null) {
            log.info(" get form redis. serial number: {}", name);
            return deserializationProduct(productJson);
        } else {
            return null;
        }
    }

    public Set<String> getAllProducts() {
        return redisTemplate.keys(String.format("*%s*", PROD_SUFFIX));
    }

    public Set<String> getProductsByCategory(String category) {
        return redisTemplate.keys(String.format("*%s*", category));
    }

    private String serializationProduct(Product product) {
        return gson.toJson(product);
    }

    private Product deserializationProduct(String productJson) {
        return gson.fromJson(productJson, Product.class);
    }
}
