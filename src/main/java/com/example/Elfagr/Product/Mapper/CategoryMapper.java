package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.CategoryDTO;
import com.example.Elfagr.Product.Entity.Category;
import org.springframework.stereotype.Component;

/**
 * Enhanced CategoryMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class CategoryMapper {

    /**
     * Converts Category entity to CategoryDTO
     * @param category The Category entity to convert
     * @return CategoryDTO object or null if input is null
     */
    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .deletedAt(category.getDeletedAt())
                .updatedAt(category.getUpdatedAt())
                .isDeleted(category.getIsDeleted())
                .build();
    }

    /**
     * Converts CategoryDTO to Category entity
     * @param categoryDTO The CategoryDTO to convert
     * @return Category entity or null if input is null
     */
    public static Category toEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .status(categoryDTO.getStatus())
                .createdAt(categoryDTO.getCreatedAt())
                .updatedAt(categoryDTO.getUpdatedAt())
                .deletedAt(categoryDTO.getDeletedAt())
                .isDeleted(categoryDTO.getIsDeleted() != null ? categoryDTO.getIsDeleted() : false)
                .build();
    }

    /**
     * Converts CategoryDTO to Category entity for updating existing categories
     * @param categoryDTO The CategoryDTO to convert
     * @param existingCategory The existing category entity to update
     * @return Updated Category entity or null if input is null
     */
    public static Category toEntityForUpdate(CategoryDTO categoryDTO, Category existingCategory) {
        if (categoryDTO == null || existingCategory == null) {
            return existingCategory;
        }

        if (categoryDTO.getName() != null) {
            existingCategory.setName(categoryDTO.getName());
        }
        if (categoryDTO.getDescription() != null) {
            existingCategory.setDescription(categoryDTO.getDescription());
        }
        if (categoryDTO.getStatus() != null) {
            existingCategory.setStatus(categoryDTO.getStatus());
        }

        return existingCategory;
    }

    /**
     * Creates a simple Category entity for basic operations
     * @param categoryDTO The CategoryDTO to convert
     * @return Category entity with minimal fields set
     */
    public static Category toEntitySimple(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        return Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .status(categoryDTO.getStatus())
                .isDeleted(false)
                .build();
    }
} 