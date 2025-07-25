package com.example.Elfagr.Product.Mapper;

import com.example.Elfagr.Product.DTO.CategoryDTO;
import com.example.Elfagr.Product.Entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryDTO toDTO(Category category){

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
//    private static Category toEntity(CategoryDTO dto){
//        return Category.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .status(dto.getStatus())
//                .createdAt(dto.getCreatedAt())
//                .updatedAt(dto.getUpdatedAt())
//                .description(dto.getDescription())
//                .deletedAt(dto.getDeletedAt())
//                .isDeleted(dto.getIsDeleted())
//                .build();
//    }
}
