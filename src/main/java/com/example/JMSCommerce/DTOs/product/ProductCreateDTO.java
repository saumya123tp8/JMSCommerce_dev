package com.example.JMSCommerce.DTOs.product;

import com.example.JMSCommerce.DTOs.productSpecification.ProductSpecificationValueDTO;
import com.example.JMSCommerce.Utility.enums.CurrencyType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {

    @NotBlank(message = "Product name is required.")
    @Size(
            min = 2,
            max = 200,
            message = "Product name must be between 2 and 200 characters."
    )
    private String name;

    @NotNull(message = "Currency is required.")
    private CurrencyType currency;

    @NotNull(message = "MRP is required.")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "MRP must be greater than or equal to 0."
    )
//    private BigDecimal mrp;
//
//    @NotNull(message = "Selling price is required.")
//    @DecimalMin(
//            value = "0.0",
//            inclusive = true,
//            message = "Selling price must be greater than or equal to 0."
//    )
//    private BigDecimal sellingPrice;

    @NotBlank(message = "Primary image is required.")
    @Size(max = 500)
    private String primaryImage;

    @Size(max = 300)
    private String shortDescription;

    @Size(max = 10000)
    private String description;

    @NotNull(message = "Category is required.")
    private Long categoryId;

    private Long brandId;

    @NotNull(message = "Available quantity is required.")
    @Min(
            value = 0,
            message = "Available quantity cannot be negative."
    )
//    private Integer availableQuantity;

//    @Size(max = 100)
//    private String sku;
//
//    @Size(max = 100)
//    private String barcode;

    private List<ProductSpecificationValueDTO> specifications;
}