package com.example.sponsoredAds.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RedisHash("Campaign")
@Data
@Builder
@AllArgsConstructor
@Log4j2
public class Campaign {

    @Id
    private String keyName;
    private String name;
    private Date startDate;
    private Double bid;
    private List<String> products;


    public boolean isActiveCampaign() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.startDate);

        calendar.add(Calendar.DAY_OF_MONTH, 10);
        Date endDate = calendar.getTime();

        return currentDate.after(startDate) && currentDate.before(endDate);
    }

    public List<String> addProductToList(Product product) {
        if (!containThisProduct(product)) {
            products.add(product.getProductName());
            return products;
        } else {
            log.info("the product {} already in the list", product.getProductName());
            return null;
        }
    }

    public void removeProduct(Product product) {
        if (!containThisProduct(product)) {
            log.info("the product - {}, not in the list", product.getProductName());
            throw new NoSuchElementException("the product, was not found in the list");
        } else {
            log.info("removing product {} from list", product.getProductName());
            products.remove(product.getProductName());
        }
    }

    private boolean containThisProduct(Product product) {
        return products.contains(product.getProductName());
    }
}
