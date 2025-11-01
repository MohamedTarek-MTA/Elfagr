package com.example.Elfagr.Product.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    @NotNull
    @Min(1)
    private Integer quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
    @JsonProperty("available")
    private Boolean isAvailable;
}
