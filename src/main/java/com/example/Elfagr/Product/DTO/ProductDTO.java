package com.example.Elfagr.Product.DTO;

import com.example.Elfagr.Product.Enum.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {
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

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;
    @NotNull
    private ProductStatus productStatus;

    private String imageUrl;

    private Integer stockQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
    @JsonProperty("available")
    private Boolean isAvailable;
}
