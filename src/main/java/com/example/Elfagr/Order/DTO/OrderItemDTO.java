package com.example.Elfagr.Order.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDTO implements Serializable {
    private Long id;
    private Long productId;
    private Long orderId;
    private Long inventoryId;
    @NotBlank
    @Min(value = 1)
    private Integer quantity;

    private BigDecimal subTotal;

    @NotBlank
    @DecimalMin(value = "0.0",inclusive = false)
    private BigDecimal unitPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
