package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Entity.ProductInventory;
import org.springframework.stereotype.Component;

@Component
public class ProductInventoryMapper {
    public static ProductInventoryDTO toDTO(ProductInventory productInventory){
        return ProductInventoryDTO.builder()
                .id(productInventory.getId())
                .inventoryId(productInventory.getInventory().getId())
                .productId(productInventory.getProduct().getId())
                .quantity(productInventory.getQuantity())
                .build();
    }
}
