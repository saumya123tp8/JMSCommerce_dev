package com.example.JMSCommerce.Utility;

import java.text.Normalizer;
import java.util.Locale;
import java.util.function.Predicate;

public final class SlugUtil {

    private SlugUtil() {}

    public static String toSlug(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Slug cannot be generated from empty text.");
        }

        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
    public static String generateUniqueSlug(
            String value,
            Predicate<String> slugExists
    ) {

        String baseSlug = toSlug(value);
        String slug = baseSlug;

        int counter = 1;

        while (slugExists.test(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }


}