package com.example.Elfagr.Inventory.DTO;

import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
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
public class InventoryTransactionDTO implements Serializable {
    private Long id;
    @NotBlank
    private Long inventoryId;
    @NotBlank
    private Long productId;
    private Long userId;
    @NotNull
    private TransactionType type;
    @NotNull
    private TransactionReason reason;
    @NotNull
    @Min(1)
    private Integer quantityChange;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
}
