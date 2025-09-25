package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Entity.ProductInventory;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Inventory.Entity.Inventory;
import org.springframework.stereotype.Component;

/**
 * Enhanced ProductInventoryMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class ProductInventoryMapper {

    /**
     * Converts ProductInventory entity to ProductInventoryDTO
     * @param productInventory The ProductInventory entity to convert
     * @return ProductInventoryDTO object or null if input is null
     */
    public static ProductInventoryDTO toDTO(ProductInventory productInventory) {
        if (productInventory == null) {
            return null;
        }

        return ProductInventoryDTO.builder()
                .id(productInventory.getId())
                .inventoryId(productInventory.getInventory() != null ? productInventory.getInventory().getId() : null)
                .productId(productInventory.getProduct() != null ? productInventory.getProduct().getId() : null)
                .quantity(productInventory.getQuantity())
                .createdAt(productInventory.getCreatedAt())
                .updatedAt(productInventory.getUpdatedAt())
                .deletedAt(productInventory.getDeletedAt())
                .isAvailable(productInventory.getIsAvailable())
                .isDeleted(productInventory.getIsDeleted())
                .build();
    }

    /**
     * Converts ProductInventoryDTO to ProductInventory entity
     * @param productInventoryDTO The ProductInventoryDTO to convert
     * @param product The product entity to associate
     * @param inventory The inventory entity to associate
     * @return ProductInventory entity or null if input is null
     */
    public static ProductInventory toEntity(ProductInventoryDTO productInventoryDTO, Product product, Inventory inventory) {
        if (productInventoryDTO == null) {
            return null;
        }

        return ProductInventory.builder()
                .id(productInventoryDTO.getId())
                .inventory(inventory)
                .product(product)
                .quantity(productInventoryDTO.getQuantity() != null ? productInventoryDTO.getQuantity() : 0)
                .createdAt(productInventoryDTO.getCreatedAt())
                .updatedAt(productInventoryDTO.getUpdatedAt())
                .deletedAt(productInventoryDTO.getDeletedAt())
                .isAvailable(productInventoryDTO.getIsAvailable())
                .isDeleted(productInventoryDTO.getIsDeleted())
                .build();
    }

    /**
     * Converts ProductInventoryDTO to ProductInventory entity for updating existing product inventories
     * @param productInventoryDTO The ProductInventoryDTO to convert
     * @param existingProductInventory The existing product inventory entity to update
     * @param product The product entity to associate (optional)
     * @param inventory The inventory entity to associate (optional)
     * @return Updated ProductInventory entity or null if input is null
     */
    public static ProductInventory toEntityForUpdate(ProductInventoryDTO productInventoryDTO, 
                                                   ProductInventory existingProductInventory, 
                                                   Product product, Inventory inventory) {
        if (productInventoryDTO == null || existingProductInventory == null) {
            return existingProductInventory;
        }

        if (productInventoryDTO.getQuantity() != null) {
            existingProductInventory.setQuantity(productInventoryDTO.getQuantity());
        }
        if (product != null) {
            existingProductInventory.setProduct(product);
        }
        if (inventory != null) {
            existingProductInventory.setInventory(inventory);
        }

        return existingProductInventory;
    }

    /**
     * Creates a simple ProductInventory entity for basic operations
     * @param productInventoryDTO The ProductInventoryDTO to convert
     * @return ProductInventory entity with minimal fields set
     */
    public static ProductInventory toEntitySimple(ProductInventoryDTO productInventoryDTO) {
        if (productInventoryDTO == null) {
            return null;
        }

        return ProductInventory.builder()
                .quantity(productInventoryDTO.getQuantity() != null ? productInventoryDTO.getQuantity() : 0)
                .build();
    }
} 