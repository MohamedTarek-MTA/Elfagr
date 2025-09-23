package com.example.Elfagr.Inventory.Mapper;

import com.example.Elfagr.Inventory.DTO.InventoryTransactionDTO;
import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Entity.Inventory;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.User.Entity.User;
import org.springframework.stereotype.Component;

/**
 * Enhanced InventoryTransactionMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class InventoryTransactionMapper {

    /**
     * Converts InventoryTransaction entity to InventoryTransactionDTO
     * @param inventoryTransaction The InventoryTransaction entity to convert
     * @return InventoryTransactionDTO object or null if input is null
     */
    public static InventoryTransactionDTO toDTO(InventoryTransaction inventoryTransaction) {
        if (inventoryTransaction == null) {
            return null;
        }

        return InventoryTransactionDTO.builder()
                .id(inventoryTransaction.getId())
                .inventoryId(inventoryTransaction.getInventory() != null ? inventoryTransaction.getInventory().getId() : null)
                .productId(inventoryTransaction.getProduct() != null ? inventoryTransaction.getProduct().getId() : null)
                .type(inventoryTransaction.getType())
                .reason(inventoryTransaction.getReason())
                .quantityChange(inventoryTransaction.getQuantityChange())
                .createdAt(inventoryTransaction.getCreatedAt())
                .updatedAt(inventoryTransaction.getUpdatedAt())
                .deletedAt(inventoryTransaction.getDeletedAt())
                .isDeleted(inventoryTransaction.getIsDeleted())
                .build();
    }

    /**
     * Converts InventoryTransactionDTO to InventoryTransaction entity
     * @param inventoryTransactionDTO The InventoryTransactionDTO to convert
     * @param inventory The inventory entity to associate
     * @param product The product entity to associate
     * @param user The user entity to associate
     * @return InventoryTransaction entity or null if input is null
     */
    public static InventoryTransaction toEntity(InventoryTransactionDTO inventoryTransactionDTO, 
                                              Inventory inventory, Product product, User user) {
        if (inventoryTransactionDTO == null) {
            return null;
        }

        return InventoryTransaction.builder()
                .id(inventoryTransactionDTO.getId())
                .type(inventoryTransactionDTO.getType())
                .reason(inventoryTransactionDTO.getReason())
                .quantityChange(inventoryTransactionDTO.getQuantityChange())
                .createdAt(inventoryTransactionDTO.getCreatedAt())
                .updatedAt(inventoryTransactionDTO.getUpdatedAt())
                .deletedAt(inventoryTransactionDTO.getDeletedAt())
                .isDeleted(inventoryTransactionDTO.getIsDeleted() != null ? inventoryTransactionDTO.getIsDeleted() : false)
                .inventory(inventory)
                .product(product)
                .user(user)
                .build();
    }

    /**
     * Converts InventoryTransactionDTO to InventoryTransaction entity for updating existing transactions
     * @param inventoryTransactionDTO The InventoryTransactionDTO to convert
     * @param existingTransaction The existing transaction entity to update
     * @param inventory The inventory entity to associate (optional)
     * @param product The product entity to associate (optional)
     * @param user The user entity to associate (optional)
     * @return Updated InventoryTransaction entity or null if input is null
     */
    public static InventoryTransaction toEntityForUpdate(InventoryTransactionDTO inventoryTransactionDTO, 
                                                       InventoryTransaction existingTransaction,
                                                       Inventory inventory, Product product, User user) {
        if (inventoryTransactionDTO == null || existingTransaction == null) {
            return existingTransaction;
        }

        if (inventoryTransactionDTO.getType() != null) {
            existingTransaction.setType(inventoryTransactionDTO.getType());
        }
        if (inventoryTransactionDTO.getReason() != null) {
            existingTransaction.setReason(inventoryTransactionDTO.getReason());
        }
        if (inventoryTransactionDTO.getQuantityChange() != null) {
            existingTransaction.setQuantityChange(inventoryTransactionDTO.getQuantityChange());
        }
        if (inventory != null) {
            existingTransaction.setInventory(inventory);
        }
        if (product != null) {
            existingTransaction.setProduct(product);
        }
        if (user != null) {
            existingTransaction.setUser(user);
        }

        return existingTransaction;
    }

    /**
     * Creates a simple InventoryTransaction entity for basic operations
     * @param inventoryTransactionDTO The InventoryTransactionDTO to convert
     * @return InventoryTransaction entity with minimal fields set
     */
    public static InventoryTransaction toEntitySimple(InventoryTransactionDTO inventoryTransactionDTO) {
        if (inventoryTransactionDTO == null) {
            return null;
        }

        return InventoryTransaction.builder()
                .type(inventoryTransactionDTO.getType())
                .reason(inventoryTransactionDTO.getReason())
                .quantityChange(inventoryTransactionDTO.getQuantityChange())
                .isDeleted(false)
                .build();
    }
} 