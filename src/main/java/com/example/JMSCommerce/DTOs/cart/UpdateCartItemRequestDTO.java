package com.example.JMSCommerce.DTOs.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCartItemRequestDTO {

    @NotNull
    @Min(1)
    private Integer quantity;

    @Builder.Default
    private List<Long> customizationOptionIds = new ArrayList<>();

}
