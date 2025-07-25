package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.Entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public static ProductDTO toDTO(Product product,Long categoryId,Long inventoryId){
        return ProductDTO.builder()
                .id(product.getId())
                .categoryId(categoryId)
                .inventoryId(inventoryId)
                .name(product.getName())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .isAvailable(product.getIsAvailable())
                .isDeleted(product.getIsDeleted())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }
}
