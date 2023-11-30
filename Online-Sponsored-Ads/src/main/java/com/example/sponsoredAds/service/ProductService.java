package com.example.sponsoredAds.service;

import com.example.sponsoredAds.models.Product;
import com.example.sponsoredAds.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        productRepository.saveProduct(product);

        return product;
    }

    public Product getProduct(String name) {
        return productRepository.getProduct(name);
    }

    public List<Product> getAllProducts() {
        Set<String> productsKeys = productRepository.getAllProducts();
        List<Product> products = new ArrayList<>();

        if (productsKeys != null) {
            products = productsKeys.stream()
                    .map(this::getProduct)
                    .filter(Objects::nonNull)
                    .toList();
        }

        return products;
    }
}
