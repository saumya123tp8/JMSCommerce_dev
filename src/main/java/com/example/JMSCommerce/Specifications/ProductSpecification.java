package com.example.JMSCommerce.Specifications;

import com.example.JMSCommerce.Model.Product;
import com.example.JMSCommerce.Model.ProductVariant;
import com.example.JMSCommerce.Utility.enums.InventoryType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

/**
 * Database-side filtering for the public product listing/search endpoint.
 *
 * This deliberately keeps the existing category endpoint untouched. Category
 * descendants are resolved by the service and supplied as categoryIds here.
 */
public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> active() {
        return (root, query, cb) ->
                cb.equal(root.get("status"), com.example.JMSCommerce.Utility.enums.ProductStatus.ACTIVE);
    }

    public static Specification<Product> text(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String pattern = "%" + value.trim().toLowerCase() + "%";

        return (root, query, cb) -> {
            Join<Product, ?> brand = root.join("brand", JoinType.LEFT);
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("shortDescription")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(brand.get("name")), pattern)
            );
        };
    }

    public static Specification<Product> categoryIds(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Product> brandId(Long brandId) {
        if (brandId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<Product> minPrice(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("sellingPrice"), value);
    }

    public static Specification<Product> maxPrice(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("sellingPrice"), value);
    }

    public static Specification<Product> minRating(Double value) {
        if (value == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), value);
    }

    public static Specification<Product> saleOnly(Boolean value) {
        if (!Boolean.TRUE.equals(value)) {
            return null;
        }
        return (root, query, cb) -> cb.lessThan(root.get("sellingPrice"), root.get("mrp"));
    }

    /**
     * In stock means:
     * - INFINITE inventory, OR
     * - FINITE inventory with at least one active variant having stock > 0.
     * PRE_ORDER and MADE_TO_ORDER are intentionally excluded.
     */
    public static Specification<Product> inStock(Boolean value) {
        if (!Boolean.TRUE.equals(value)) {
            return null;
        }

        return (root, query, cb) -> {
            Predicate infinite = cb.equal(root.get("inventoryType"), InventoryType.INFINITE);

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<ProductVariant> variant = subquery.from(ProductVariant.class);
            subquery.select(cb.literal(1L));
            subquery.where(
                    cb.equal(variant.get("product").get("id"), root.get("id")),
                    cb.isTrue(variant.get("active")),
                    cb.greaterThan(variant.get("stock"), 0)
            );

            Predicate finiteWithStock = cb.and(
                    cb.equal(root.get("inventoryType"), InventoryType.FINITE),
                    cb.exists(subquery)
            );

            return cb.or(infinite, finiteWithStock);
        };
    }
}
