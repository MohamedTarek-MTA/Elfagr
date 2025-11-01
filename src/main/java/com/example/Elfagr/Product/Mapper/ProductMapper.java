package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Product.Entity.Category;
import org.springframework.stereotype.Component;

/**
 * Enhanced ProductMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class ProductMapper {

    /**
     * Converts Product entity to ProductDTO
     * @param product The Product entity to convert
     * @param categoryId The category ID to include in DTO
     * @param inventoryId The inventory ID to include in DTO
     * @return ProductDTO object or null if input is null
     */
    public static ProductDTO toDTO(Product product, Long categoryId, Long inventoryId) {
        if (product == null) {
            return null;
        }

        return ProductDTO.builder()
                .id(product.getId())
                .categoryId(categoryId != null ? categoryId : 
                    (product.getCategory() != null ? product.getCategory().getId() : null))
                .inventoryId(inventoryId)
                .name(product.getName())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .description(product.getDescription())
                .productStatus(product.getProductStatus())
                .imageUrl(product.getImageUrl())
                .isAvailable(product.getIsAvailable())
                .isDeleted(product.getIsDeleted())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .deletedAt(product.getDeletedAt())
                .build();
    }

    /**
     * Converts Product entity to ProductDTO (simplified version)
     * @param product The Product entity to convert
     * @return ProductDTO object or null if input is null
     */
    public static ProductDTO toDTO(Product product) {
        return toDTO(product, null, null);
    }

    /**
     * Converts ProductDTO to Product entity
     * @param productDTO The ProductDTO to convert
     * @param category The category entity to associate
     * @return Product entity or null if input is null
     */
    public static Product toEntity(ProductDTO productDTO, Category category) {
        if (productDTO == null) {
            return null;
        }

        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .barcode(productDTO.getBarcode())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .productStatus(productDTO.getProductStatus())
                .isAvailable(productDTO.getIsAvailable() != null ? productDTO.getIsAvailable() : true)
                .isDeleted(productDTO.getIsDeleted() != null ? productDTO.getIsDeleted() : false)
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .deletedAt(productDTO.getDeletedAt())
                .category(category)
                .build();
    }

    /**
     * Converts ProductDTO to Product entity for updating existing products
     * @param productDTO The ProductDTO to convert
     * @param existingProduct The existing product entity to update
     * @param category The category entity to associate (optional)
     * @return Updated Product entity or null if input is null
     */
    public static Product toEntityForUpdate(ProductDTO productDTO, Product existingProduct, Category category) {
        if (productDTO == null || existingProduct == null) {
            return existingProduct;
        }

        if (productDTO.getName() != null) {
            existingProduct.setName(productDTO.getName());
        }
        if (productDTO.getSku() != null) {
            existingProduct.setSku(productDTO.getSku());
        }
        if (productDTO.getBarcode() != null) {
            existingProduct.setBarcode(productDTO.getBarcode());
        }
        if (productDTO.getPrice() != null) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        if (productDTO.getDescription() != null) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (productDTO.getImageUrl() != null) {
            existingProduct.setImageUrl(productDTO.getImageUrl());
        }
        if (productDTO.getIsAvailable() != null) {
            existingProduct.setIsAvailable(productDTO.getIsAvailable());
        }
        if (category != null) {
            existingProduct.setCategory(category);
        }

        return existingProduct;
    }

    /**
     * Creates a simple Product entity for basic operations
     * @param productDTO The ProductDTO to convert
     * @return Product entity with minimal fields set
     */
    public static Product toEntitySimple(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }

        return Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .barcode(productDTO.getBarcode())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .imageUrl(productDTO.getImageUrl())
                .isAvailable(true)
                .isDeleted(false)
                .build();
    }
} 