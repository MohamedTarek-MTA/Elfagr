package com.example.Elfagr.Return.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnItemDTO {
    private Long id;
    @NotBlank
    private Long productId;
    @NotBlank
    private Long inventoryId;
    @NotBlank
    @DecimalMin(value = "0.0",inclusive = false)
    private BigDecimal subTotal;

    @NotBlank
    @Min(1)
    private Integer quantity;
}
