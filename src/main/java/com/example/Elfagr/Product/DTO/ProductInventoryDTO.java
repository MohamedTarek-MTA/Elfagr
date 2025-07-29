package com.example.Elfagr.Product.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInventoryDTO implements Serializable {
    private Long id;
    @NotBlank
    private Long productId;
    @NotBlank
    private Long inventoryId;
    @NotBlank
    @Min(1)
    private Integer quantity;
}
