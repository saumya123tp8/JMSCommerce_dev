package com.example.JMSCommerce.DTOs;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Setter
@Getter
public class GetOrderResponseDTO {
    private String orderStatus;
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String deliveredAt;
    private BigDecimal currentSubtotal;
    private List<OrderItemResponseDTO> orderItems;
}
