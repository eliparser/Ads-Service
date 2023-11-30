package com.example.sponsoredAds.utils;

import com.example.sponsoredAds.models.Product;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class Utils {
    public static String PROD_SUFFIX = "prod";
    public static String CAMP_ID_SUFFIX = "campaign-ID";

    private static final List<String> categories = List.of(
            "Clothing", "Education", "Electronics", "Footwear", "Fruits");

    private static final List<String> randomNames = List.of(
            "Toyota", "Apple", "Nike", "Samsung", "Coca-Cola",
            "Ford", "Orange", "Adidas", "Sony", "Pepsi",
            "Mercedes", "Banana", "Gucci", "Panasonic", "Sprite",
            "BMW", "Grapes", "Zara", "LG", "Fanta",
            "Audi", "Watermelon", "H&M", "Philips", "RedBull",
            "Honda", "Pineapple", "Versace", "Sharp", "Monster"
    );

    public static List<Product> createProductsForInit() {
        List<Product> products = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            double price = 10 + (100 - 10) * random.nextDouble();

            Product product = Product.builder().build();
            product.setProductName(generateRandomName());
            product.setSerialNumber(UUID.randomUUID().toString());
            product.setCategory(categories.get(random.nextInt(categories.size())));
            product.setTitle("Random Title");
            product.setPrice(price);

            products.add(product);
        }

        return products;
    }

    private static String generateRandomName() {
        Random random = new Random();
        return randomNames.get(random.nextInt(randomNames.size()));
    }
}
