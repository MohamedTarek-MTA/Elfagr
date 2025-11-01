package com.example.Elfagr.Return.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnItemDTO implements Serializable {
    private Long id;
    @NotBlank
    private Long productId;
    @NotBlank
    private Long inventoryId;
    @NotNull
    @DecimalMin(value = "0.0",inclusive = false)
    private BigDecimal subTotal;

    @NotNull
    @Min(1)
    private Integer quantity;
}
