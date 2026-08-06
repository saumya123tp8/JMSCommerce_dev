package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductVariant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductPriceSyncService {
    @Transactional
    public void syncPrices(Product product) {

        List<ProductVariant> activeVariants =
                product.getVariants()
                        .stream()
                        .filter(ProductVariant::getActive)
                        .toList();

        if (activeVariants.isEmpty()) {

            product.setSellingPrice(null);
            product.setMrp(null);

            return;
        }

        BigDecimal minimumSellingPrice =
                activeVariants.stream()
                        .map(ProductVariant::getSellingPrice)
                        .min(BigDecimal::compareTo)
                        .orElse(null);

        BigDecimal minimumMrp =
                activeVariants.stream()
                        .map(ProductVariant::getMrp)
                        .min(BigDecimal::compareTo)
                        .orElse(null);

        product.setSellingPrice(minimumSellingPrice);
        product.setMrp(minimumMrp);

    }
}
