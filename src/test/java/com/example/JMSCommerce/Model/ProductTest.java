package com.example.JMSCommerce.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldAddVariant() {

        Product product = new Product();
        ProductVariant variant = new ProductVariant();

        product.addVariant(variant);

        assertEquals(1, product.getVariants().size());
        assertSame(variant, product.getVariants().get(0));
        assertSame(product, variant.getProduct());
    }

    @Test
    void shouldRemoveVariant() {

        Product product = new Product();
        ProductVariant variant = new ProductVariant();

        product.addVariant(variant);
        product.removeVariant(variant);

        assertTrue(product.getVariants().isEmpty());
        assertNull(variant.getProduct());
    }

    @Test
    void shouldAddCustomizationGroup() {

        Product product = new Product();
        CustomizationGroup group = new CustomizationGroup();

        product.addCustomizationGroup(group);

        assertEquals(1, product.getCustomizationGroups().size());
        assertSame(group, product.getCustomizationGroups().get(0));
        assertSame(product, group.getProduct());
    }

    @Test
    void shouldRemoveCustomizationGroup() {

        Product product = new Product();
        CustomizationGroup group = new CustomizationGroup();

        product.addCustomizationGroup(group);
        product.removeCustomizationGroup(group);

        assertTrue(product.getCustomizationGroups().isEmpty());
        assertNull(group.getProduct());
    }
}