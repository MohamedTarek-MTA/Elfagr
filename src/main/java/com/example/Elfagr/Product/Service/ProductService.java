package com.example.Elfagr.Product.Service;

import com.example.Elfagr.Inventory.Mapper.InventoryMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Product.Mapper.ProductMapper;
import com.example.Elfagr.Product.Repository.CategoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.Shared.Service.UploadImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final UploadImageService uploadImageService;
    @Transactional
    public ProductDTO createProduct(Long categoryId, ProductDTO dto, MultipartFile image){
        var category = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("Category Not Found !"));
        var product = Product.builder()
                .category(category)
                .name(dto.getName())
                .price(dto.getPrice())
                .sku(dto.getSku())
                .barcode(dto.getBarcode())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .stockQuantity(dto.getStockQuantity())
                .isAvailable(dto.getIsAvailable())
                .build();
        if(image != null && !image.isEmpty()){
           String imageUrl = uploadImageService.uploadMultipartFile(image);
           product.setImageUrl(imageUrl);
        }
        productRepository.save(product);
        return ProductMapper.toDTO(product);
    }

}
