package com.example.Elfagr.Inventory.Mapper;

import com.example.Elfagr.Inventory.DTO.InventoryDTO;
import com.example.Elfagr.Inventory.Entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {
    public static InventoryDTO toDTO(Inventory inventory){
        return InventoryDTO.builder()
                .id(inventory.getId())
                .name(inventory.getName())
                .contactInfo(inventory.getContactInfo())
                .inventoryType(inventory.getType())
                .status(inventory.getStatus())
                .description(inventory.getDescription())
                .address(inventory.getAddress())
                .updatedAt(inventory.getUpdatedAt())
                .createdAt(inventory.getCreatedAt())
                .deletedAt(inventory.getDeletedAt())
                .isDeleted(inventory.getIsDeleted())
                .build();
    }
}
