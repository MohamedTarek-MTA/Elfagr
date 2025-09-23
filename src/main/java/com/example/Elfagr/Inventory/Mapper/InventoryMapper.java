package com.example.Elfagr.Inventory.Mapper;

import com.example.Elfagr.Inventory.DTO.InventoryDTO;
import com.example.Elfagr.Inventory.Entity.Inventory;
import org.springframework.stereotype.Component;

/**
 * Enhanced InventoryMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class InventoryMapper {

    /**
     * Converts Inventory entity to InventoryDTO
     * @param inventory The Inventory entity to convert
     * @return InventoryDTO object or null if input is null
     */
    public static InventoryDTO toDTO(Inventory inventory) {
        if (inventory == null) {
            return null;
        }

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

    /**
     * Converts InventoryDTO to Inventory entity
     * @param inventoryDTO The InventoryDTO to convert
     * @return Inventory entity or null if input is null
     */
    public static Inventory toEntity(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) {
            return null;
        }

        return Inventory.builder()
                .Id(inventoryDTO.getId())
                .name(inventoryDTO.getName())
                .description(inventoryDTO.getDescription())
                .address(inventoryDTO.getAddress())
                .contactInfo(inventoryDTO.getContactInfo())
                .createdAt(inventoryDTO.getCreatedAt())
                .updatedAt(inventoryDTO.getUpdatedAt())
                .deletedAt(inventoryDTO.getDeletedAt())
                .isDeleted(inventoryDTO.getIsDeleted() != null ? inventoryDTO.getIsDeleted() : false)
                .status(inventoryDTO.getStatus())
                .type(inventoryDTO.getInventoryType())
                .build();
    }

    /**
     * Converts InventoryDTO to Inventory entity for updating existing inventories
     * @param inventoryDTO The InventoryDTO to convert
     * @param existingInventory The existing inventory entity to update
     * @return Updated Inventory entity or null if input is null
     */
    public static Inventory toEntityForUpdate(InventoryDTO inventoryDTO, Inventory existingInventory) {
        if (inventoryDTO == null || existingInventory == null) {
            return existingInventory;
        }

        if (inventoryDTO.getName() != null) {
            existingInventory.setName(inventoryDTO.getName());
        }
        if (inventoryDTO.getDescription() != null) {
            existingInventory.setDescription(inventoryDTO.getDescription());
        }
        if (inventoryDTO.getAddress() != null) {
            existingInventory.setAddress(inventoryDTO.getAddress());
        }
        if (inventoryDTO.getContactInfo() != null) {
            existingInventory.setContactInfo(inventoryDTO.getContactInfo());
        }
        if (inventoryDTO.getStatus() != null) {
            existingInventory.setStatus(inventoryDTO.getStatus());
        }
        if (inventoryDTO.getInventoryType() != null) {
            existingInventory.setType(inventoryDTO.getInventoryType());
        }

        return existingInventory;
    }

    /**
     * Creates a simple Inventory entity for basic operations
     * @param inventoryDTO The InventoryDTO to convert
     * @return Inventory entity with minimal fields set
     */
    public static Inventory toEntitySimple(InventoryDTO inventoryDTO) {
        if (inventoryDTO == null) {
            return null;
        }

        return Inventory.builder()
                .name(inventoryDTO.getName())
                .description(inventoryDTO.getDescription())
                .address(inventoryDTO.getAddress())
                .contactInfo(inventoryDTO.getContactInfo())
                .status(inventoryDTO.getStatus())
                .type(inventoryDTO.getInventoryType())
                .isDeleted(false)
                .build();
    }
} 