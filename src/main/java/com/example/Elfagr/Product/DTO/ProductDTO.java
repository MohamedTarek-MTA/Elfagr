package com.example.Elfagr.Product.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    @NotBlank
    private Long categoryId;
    @NotBlank
    private Long inventoryId;
    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "sku" , unique = true)
    private String sku;

    @NotBlank
    @Column(name = "barcode" , unique = true)
    private String barcode;

    private String description;

    @NotBlank
    private BigDecimal price;

    private String imageUrl;

    private Integer stockQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Boolean isDeleted;

    private Boolean isAvailable;
}
