package com.example.JMSCommerce.DTOs.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponseDTO {
    private List<ProductResponseDetailsDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
