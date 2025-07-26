package com.example.Elfagr.Inventory.DTO;

import com.example.Elfagr.Inventory.Enum.InventoryType;
import com.example.Elfagr.Inventory.Enum.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDTO implements Serializable {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String address;
    @NotBlank
    private String contactInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;
    @NotBlank
    private Status status;
    @NotBlank
    private InventoryType inventoryType;
}
