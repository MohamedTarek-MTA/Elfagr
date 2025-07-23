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
    public static Inventory toEntity(InventoryDTO dto){
        return Inventory.builder()
                .Id(dto.getId())
                .name(dto.getName())
                .contactInfo(dto.getContactInfo())
                .type(dto.getInventoryType())
                .status(dto.getStatus())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .deletedAt(dto.getDeletedAt())
                .isDeleted(dto.getIsDeleted())
                .build();
    }
}
