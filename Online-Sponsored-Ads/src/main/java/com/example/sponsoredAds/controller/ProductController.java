package com.example.sponsoredAds.controller;

import com.example.sponsoredAds.models.Product;
import com.example.sponsoredAds.service.CampaignService;
import com.example.sponsoredAds.service.ProductService;
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
import java.util.UUID;

import static com.example.sponsoredAds.utils.Utils.PROD_SUFFIX;
import static com.example.sponsoredAds.utils.Utils.createProductsForInit;

@RestController
@Log4j2
public class ProductController {
    private final ProductService productService;
    private final CampaignService campaignService;
    private final Gson gson;

    public ProductController(ProductService productService, CampaignService campaignService) {
        this.productService = productService;
        this.campaignService = campaignService;
        gson = new Gson();
    }

    @PostMapping("/products")
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        updateFields(product);

        Product savedProduct = productService.saveProduct(product);

        if (savedProduct == null) {
            log.info("Error saving product in redis");
            return ResponseEntity.badRequest().build();
        } else {
            log.info("Product - {}", savedProduct);
            return ResponseEntity.accepted().body(savedProduct);
        }
    }

    @GetMapping("/serveAd/{category}")
    public ResponseEntity<Object> getPromotedProductByCategory(@PathVariable String category) {
        Product promotedProduct = campaignService.getPromotedProductByCategory(category);

        if (promotedProduct != null) {
            log.info("ad for category - {}", category);
            String productJson = gson.toJson(promotedProduct);

            return new ResponseEntity<>(productJson, HttpStatus.OK);
        } else {
            log.error("No promoted product found for category - {}", category);
            return new ResponseEntity<>("No promoted product found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/product/{name}")
    public ResponseEntity<Object> getProduct(@PathVariable String name) {
        Product product = productService.getProduct(name);

        if (product != null) {
            log.info("Retrieved product - {}", product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            log.error("Product not found for this name - {}", name);
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allProducts")
    public ResponseEntity<Object> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();

        if (!allProducts.isEmpty()) {
            log.info("Retrieved all campaigns - {}", allProducts);
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        } else {
            log.error("No campaigns found");
            return new ResponseEntity<>("No campaigns found", HttpStatus.NOT_FOUND);
        }
    }

    //creating Products for testing
    @PostMapping("/products/test")
    public ResponseEntity<Object> sendProducts() {
        List<Product> products = createProductsForInit();

        for (Product product : products) {
            updateFields(product);

            Product savedProduct = productService.saveProduct(product);

            if (savedProduct != null) {
                log.info("Product - {}", savedProduct);
            } else {
                log.info("Error saving product in redis");
                return ResponseEntity.badRequest().body("Error saving products in Redis");
            }
        }

        return ResponseEntity.accepted().build();
    }


    private void updateFields(Product product) {
        //add category to the key
        product.setProductName(
                String.format("%s-%s-%s", product.getProductName(), product.getCategory(), PROD_SUFFIX));

        if (product.getSerialNumber() == null) {
            product.setSerialNumber(UUID.randomUUID().toString());
        }
    }
}
