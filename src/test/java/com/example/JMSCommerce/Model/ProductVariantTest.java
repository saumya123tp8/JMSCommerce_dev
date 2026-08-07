package com.example.JMSCommerce.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductVariantTest {

    @Test
    void shouldAddAttribute() {

        VariantAttribute variantAttribute = new VariantAttribute();
        ProductVariant variant = new ProductVariant();

        variant.addAttribute(variantAttribute);

        assertEquals(1, variant.getAttributes().size());
        assertSame(
                variantAttribute,
                variant.getAttributes().get(0)
        );
    }

    @Test
    void shouldRemoveAttribute() {

        VariantAttribute variantAttribute = new VariantAttribute();
        ProductVariant variant = new ProductVariant();

        variant.addAttribute(variantAttribute);
        variant.removeAttribute(variantAttribute);

        assertEquals(0, variant.getAttributes().size());
//        assertSame(
//                variantAttribute,
//                variant.getAttributes().get(0)
//        );
    }

}
