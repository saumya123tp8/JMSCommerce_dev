package com.example.JMSCommerce.Services;

import com.example.JMSCommerce.Exception.BadRequestException;
import com.example.JMSCommerce.Repositories.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl
        implements InventoryService {

    private final ProductVariantRepository variantRepository;

    @Override
    public void reserve(
            Long variantId,
            Integer quantity
    ) {

        int updated =
                variantRepository.reserveStock(
                        variantId,
                        quantity
                );

        if (updated == 0) {
            throw new BadRequestException(
                    "Insufficient stock for variant: "
                            + variantId
            );
        }
    }

    @Override
    public void release(
            Long variantId,
            Integer quantity
    ) {

        int updated =
                variantRepository.releaseStock(
                        variantId,
                        quantity
                );

        if (updated == 0) {
            throw new IllegalStateException(
                    "Unable to release inventory for variant: "
                            + variantId
            );
        }
    }
}
