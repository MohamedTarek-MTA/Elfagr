package com.example.Elfagr.Product.Service;

import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Product.Entity.ProductInventory;
import com.example.Elfagr.Product.Mapper.ProductMapper;
import com.example.Elfagr.Product.Repository.CategoryRepository;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.Shared.Service.UploadImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final UploadImageService uploadImageService;
    private final ProductInventoryRepository productInventoryRepository;
    @Transactional
    public ProductDTO createProduct(ProductDTO dto, MultipartFile image){
        if(dto.getCategoryId() == null || dto.getInventoryId() == null)
            throw new IllegalArgumentException("Please Choose Product Category and Inventory !");
        var category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category Not Found !"));
        var inventory = inventoryRepository.findById(dto.getInventoryId()).orElseThrow(()->new IllegalArgumentException("Inventory Not Found !"));
        var product = productRepository.findBySkuOrBarcode(dto.getSku(),dto.getBarcode()).orElseGet(()->{
          Product newProduct =  Product.builder()
                    .category(category)
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .sku(dto.getSku())
                    .barcode(dto.getBarcode())
                    .description(dto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .isAvailable(dto.getIsAvailable())
                    .build();
            if(image != null && !image.isEmpty()){
                String imageUrl = uploadImageService.uploadMultipartFile(image);
                newProduct.setImageUrl(imageUrl);
            }
          return productRepository.save(newProduct);
        });
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventory.getId()).orElseGet(()->{
            ProductInventory newProductInventory = ProductInventory.builder()
                    .product(product)
                    .quantity(dto.getStockQuantity())
                    .inventory(inventory)
                    .build();
           return productInventoryRepository.save(newProductInventory);
        });

       ProductDTO productDTO = ProductMapper.toDTO(product,category.getId(),inventory.getId());
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }

}
