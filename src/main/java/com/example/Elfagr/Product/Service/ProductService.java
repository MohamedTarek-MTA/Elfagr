package com.example.Elfagr.Product.Service;

import com.example.Elfagr.Inventory.DTO.InventoryDTO;
import com.example.Elfagr.Inventory.Mapper.InventoryMapper;
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
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
                    .createdAt(LocalDateTime.now())
                    .isAvailable(dto.getIsAvailable())
                    .build();
           return productInventoryRepository.save(newProductInventory);
        });

       ProductDTO productDTO = ProductMapper.toDTO(product,category.getId(),inventory.getId());
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Transactional
    @CachePut(key = "#productId",value = "products")
    public String updateProduct(Long productId,ProductDTO dto,MultipartFile image){
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        Optional.ofNullable(dto.getName()).ifPresent(product::setName);
        Optional.ofNullable(dto.getPrice()).ifPresent(product::setPrice);
        Optional.ofNullable(dto.getSku()).ifPresent(product::setSku);
        Optional.ofNullable(dto.getDescription()).ifPresent(product::setDescription);
        if(image != null && !image.isEmpty()){
            String imageUrl = uploadImageService.uploadMultipartFile(image);
            product.setImageUrl(imageUrl);
        }
        product.setUpdatedAt(LocalDateTime.now());
        return "Product Successfully Updated !";
    }
    @Transactional
    @CachePut(value = "products",key = "#productId")
    public String changeProductCategory(Long categoryId,Long productId){
        var category = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("Category Not Found !"));
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());
        return "Product Category Successfully Updated !";
    }
    @Transactional
    public String changeProductStockQuantity(Long productId,Long inventoryId,Integer quantity){
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(productId,inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found in this Inventory !"));
        productInventory.setQuantity(quantity);
        productInventoryRepository.save(productInventory);
        return "Product Stock Quantity has been Changed !";
    }
    public Page<ProductDTO> getAllProducts(Pageable pageable){
        var products = productRepository.findAll(pageable);
        return products.map(ProductMapper::toDTO);
    }
    @Cacheable(value = "products",key = "#productId")
    public ProductDTO getProductById(Long productId){
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventories = productInventoryRepository.findByProductId(productId,null);
        Integer totalAmount = 0;
        for(var productInventory : productInventories){
            totalAmount += productInventory.getQuantity();
        }
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),null);
        productDTO.setStockQuantity(totalAmount);
        return productDTO;
    }
    public Page<ProductDTO> getProductsByCategoryId(Long categoryId,Pageable pageable){
        return productRepository.findByCategoryId(categoryId,pageable).map(product -> ProductMapper.toDTO(product,categoryId,null));
    }
    public List<InventoryDTO> getInventoriesByProductId(Long productId){
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventories = productInventoryRepository.findByProductId(productId,null);
        return productInventories.stream().map(inventory ->
                InventoryMapper.toDTO(
                        inventoryRepository.findById(
                                inventory.getId()).orElseThrow(
                                        ()->new IllegalArgumentException("Inventory Not Found !")))).toList();
    }
}
