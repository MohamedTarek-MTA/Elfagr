package com.example.Elfagr.Inventory.Mapper;

import com.example.Elfagr.Inventory.DTO.InventoryTransactionDTO;
import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper {
    public static InventoryTransactionDTO toDTO(InventoryTransaction inventoryTransaction){
        return InventoryTransactionDTO.builder()
                .id(inventoryTransaction.getId())
                .inventoryId(inventoryTransaction.getInventory().getId())
                .productId(inventoryTransaction.getProduct().getId())
                .type(inventoryTransaction.getType())
                .reason(inventoryTransaction.getReason())
                .quantityChange(inventoryTransaction.getQuantityChange())
                .createdAt(inventoryTransaction.getCreatedAt())
                .updatedAt(inventoryTransaction.getUpdatedAt())
                .deletedAt(inventoryTransaction.getDeletedAt())
                .isDeleted(inventoryTransaction.getIsDeleted())
                .build();
    }
}
