package com.example.Elfagr.Product.Service;

import com.example.Elfagr.Product.DTO.CategoryDTO;
import com.example.Elfagr.Product.Entity.Category;
import com.example.Elfagr.Product.Enum.CategoryStatus;
import com.example.Elfagr.Product.Mapper.CategoryMapper;
import com.example.Elfagr.Product.Repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        var category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .createdAt(LocalDateTime.now())
                .updatedAt(null)
                .deletedAt(null)
                .isDeleted(null)
                .build();
        categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }

    @Transactional
    @CachePut(value = "categories", key = "#id")
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category Not Found !"));

        Optional.ofNullable(dto.getName()).ifPresent(category::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(category::setDescription);
        Optional.ofNullable(dto.getStatus()).ifPresent(category::setStatus);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }

    @Cacheable(value = "categories", key = "#id")
    public CategoryDTO getCategoryById(Long id) {

        return CategoryMapper.toDTO(
                categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category Not Found !"))
        );
    }

    @Transactional
    private CategoryDTO updateCategoryStatus(Long id, CategoryStatus status, Boolean isDeleted) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category Not Found !"));

        category.setStatus(status);
        category.setUpdatedAt(LocalDateTime.now());
        category.setIsDeleted(isDeleted != null ? isDeleted : category.getIsDeleted());

        if (Boolean.TRUE.equals(isDeleted)) {
            category.setDeletedAt(LocalDateTime.now());
        }
        categoryRepository.save(category);

        return CategoryMapper.toDTO(category);
    }

    @CachePut(value = "categories",key = "#id")
    public CategoryDTO setAsAvailableCategory(Long id){
        return updateCategoryStatus(id,CategoryStatus.AVAILABLE,false);
    }

    @CachePut(value = "categories",key = "#id")
    public CategoryDTO setAsNotAvailableCategory(Long id){
        return updateCategoryStatus(id,CategoryStatus.NOT_AVAILABLE,false);
    }

    @CacheEvict(value = "categories",key = "#id")
    public CategoryDTO deleteCategory(Long id){
        return updateCategoryStatus(id,CategoryStatus.NOT_AVAILABLE,true);
    }

    public Page<CategoryDTO> getAllCategories(Pageable pageable){
        return (categoryRepository.findAll(pageable)).map(CategoryMapper::toDTO);

    }

   private Page<CategoryDTO> getByStatus(CategoryStatus status,Pageable pageable){
        return (categoryRepository.findByStatus(status,pageable)).map(CategoryMapper::toDTO);
   }

   @Cacheable(value = "categories",key = "#name")
   public Page<CategoryDTO> getByName(String name,Pageable pageable){
        return categoryRepository.findByNameContainingIgnoreCase(name,pageable).map(CategoryMapper::toDTO);
   }

   public Page<CategoryDTO> getAvailableCategories(Pageable pageable){
        return getByStatus(CategoryStatus.AVAILABLE,pageable);
   }
    public Page<CategoryDTO> getNotAvailableCategories(Pageable pageable){
        return getByStatus(CategoryStatus.NOT_AVAILABLE,pageable);
    }

}