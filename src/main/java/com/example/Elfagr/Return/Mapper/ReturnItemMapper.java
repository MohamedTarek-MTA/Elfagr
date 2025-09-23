package com.example.Elfagr.Return.Mapper;

import com.example.Elfagr.Return.DTO.ReturnItemDTO;
import com.example.Elfagr.Return.Entity.ReturnItem;
import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Inventory.Entity.Inventory;
import org.springframework.stereotype.Component;

/**
 * Enhanced ReturnItemMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class ReturnItemMapper {

    /**
     * Converts ReturnItem entity to ReturnItemDTO
     * @param returnItem The ReturnItem entity to convert
     * @return ReturnItemDTO object or null if input is null
     */
    public static ReturnItemDTO toDTO(ReturnItem returnItem) {
        if (returnItem == null) {
            return null;
        }

        return ReturnItemDTO.builder()
                .id(returnItem.getId())
                .inventoryId(returnItem.getInventory() != null ? returnItem.getInventory().getId() : null)
                .productId(returnItem.getProduct() != null ? returnItem.getProduct().getId() : null)
                .quantity(returnItem.getQuantity())
                .subTotal(returnItem.getSubTotal())
                .build();
    }

    /**
     * Converts ReturnItemDTO to ReturnItem entity
     * @param returnItemDTO The ReturnItemDTO to convert
     * @param aReturn The return entity to associate
     * @param product The product entity to associate
     * @param inventory The inventory entity to associate
     * @return ReturnItem entity or null if input is null
     */
    public static ReturnItem toEntity(ReturnItemDTO returnItemDTO, Return aReturn, Product product, Inventory inventory) {
        if (returnItemDTO == null) {
            return null;
        }

        return ReturnItem.builder()
                .id(returnItemDTO.getId())
                .subTotal(returnItemDTO.getSubTotal())
                .quantity(returnItemDTO.getQuantity())
                .product(product)
                .inventory(inventory)
                .aReturn(aReturn)
                .build();
    }

    /**
     * Converts ReturnItemDTO to ReturnItem entity for updating existing return items
     * @param returnItemDTO The ReturnItemDTO to convert
     * @param existingReturnItem The existing return item entity to update
     * @param product The product entity to associate (optional)
     * @param inventory The inventory entity to associate (optional)
     * @return Updated ReturnItem entity or null if input is null
     */
    public static ReturnItem toEntityForUpdate(ReturnItemDTO returnItemDTO, ReturnItem existingReturnItem, 
                                             Product product, Inventory inventory) {
        if (returnItemDTO == null || existingReturnItem == null) {
            return existingReturnItem;
        }

        if (returnItemDTO.getSubTotal() != null) {
            existingReturnItem.setSubTotal(returnItemDTO.getSubTotal());
        }
        if (returnItemDTO.getQuantity() != null) {
            existingReturnItem.setQuantity(returnItemDTO.getQuantity());
        }
        if (product != null) {
            existingReturnItem.setProduct(product);
        }
        if (inventory != null) {
            existingReturnItem.setInventory(inventory);
        }

        return existingReturnItem;
    }

    /**
     * Creates a simple ReturnItem entity for basic operations
     * @param returnItemDTO The ReturnItemDTO to convert
     * @return ReturnItem entity with minimal fields set
     */
    public static ReturnItem toEntitySimple(ReturnItemDTO returnItemDTO) {
        if (returnItemDTO == null) {
            return null;
        }

        return ReturnItem.builder()
                .subTotal(returnItemDTO.getSubTotal())
                .quantity(returnItemDTO.getQuantity())
                .build();
    }
} 