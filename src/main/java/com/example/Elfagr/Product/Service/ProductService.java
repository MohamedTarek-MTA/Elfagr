package com.example.Elfagr.Product.Service;


import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Inventory.Mapper.InventoryTransactionMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Inventory.Service.InventoryTransactionService;
import com.example.Elfagr.Product.DTO.ProductDTO;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Product.Entity.ProductInventory;
import com.example.Elfagr.Product.Enum.CategoryStatus;
import com.example.Elfagr.Product.Enum.ProductStatus;
import com.example.Elfagr.Product.Mapper.ProductInventoryMapper;
import com.example.Elfagr.Product.Mapper.ProductMapper;
import com.example.Elfagr.Product.Repository.CategoryRepository;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.Shared.Service.UploadImageService;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final UploadImageService uploadImageService;
    private final ProductInventoryRepository productInventoryRepository;
    private final InventoryTransactionService inventoryTransactionService;
    private final UserRepository userRepository;
    @Transactional
    public ProductDTO createProduct(Long userId,ProductDTO dto, MultipartFile image){
        if(dto.getCategoryId() == null || dto.getInventoryId() == null)
            throw new IllegalArgumentException("Please Choose Product Category and Inventory !");
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category Not Found !"));
        var inventory = inventoryRepository.findById(dto.getInventoryId()).orElseThrow(()->new IllegalArgumentException("Inventory Not Found !"));
        if(category.getStatus().equals(CategoryStatus.NOT_AVAILABLE)){
            throw new IllegalArgumentException("Sorry This Category Is Currently Unavailable !");
        }
        if(!inventory.getStatus().equals(Status.ACTIVE)){
            throw new IllegalArgumentException("Sorry This Inventory Is Not Working Please Try Again Later !");
        }
        var product = productRepository.findBySkuAndBarcodeAndProductStatus(dto.getSku(),dto.getBarcode(),dto.getProductStatus()).orElseGet(()->{
          Product newProduct =  Product.builder()
                    .category(category)
                    .name(dto.getName())
                    .price(dto.getPrice())
                    .sku(dto.getSku())
                    .barcode(dto.getBarcode())
                    .description(dto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .isAvailable(dto.getIsAvailable())
                    .productStatus(dto.getProductStatus())
                    .build();
            if(image != null && !image.isEmpty()){
                String imageUrl = uploadImageService.uploadMultipartFile(image);
                newProduct.setImageUrl(imageUrl);
            }
          return productRepository.save(newProduct);
        });
        product.setPrice(dto.getPrice());
        productRepository.save(product);
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
        var inventoryTransaction = InventoryTransaction.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .inventory(inventory)
                .product(product)
                .reason(TransactionReason.CREATED)
                .type(TransactionType.IN)
                .quantityChange(dto.getStockQuantity())
                .build();
        inventoryTransactionService.createInventoryTransaction(userId,InventoryTransactionMapper.toDTO(inventoryTransaction));

       ProductDTO productDTO = ProductMapper.toDTO(product,category.getId(),inventory.getId());
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Transactional
    @CachePut(key = "#productId",value = "products")
    public ProductDTO updateProduct(Long productId,ProductDTO dto,MultipartFile image){
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
        return ProductMapper.toDTO(product);
    }
    @Transactional
    @CachePut(value = "products",key = "#productId")
    public ProductDTO changeProductCategory(Long newCategoryId,Long productId){
        var newCategory = categoryRepository.findById(newCategoryId).orElseThrow(()->new IllegalArgumentException("Category Not Found !"));
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        product.setCategory(newCategory);
        product.setUpdatedAt(LocalDateTime.now());
        return ProductMapper.toDTO(product);
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

    @Cacheable(value = "products",key = "T(String).valueOf(#sku).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getNewProductBySkuAndInventoryId(String sku,Long inventoryId){
        var product = productRepository.findBySkuAndProductStatus(sku,ProductStatus.NEW).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Cacheable(value = "products",key = "T(String).valueOf(#sku).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getImportedProductBySkuAndInventoryId(String sku,Long inventoryId){
        var product = productRepository.findBySkuAndProductStatus(sku,ProductStatus.IMPORTED).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Cacheable(value = "products",key = "T(String).valueOf(#barcode).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getNewProductByBarcodeAndInventoryId(String barcode,Long inventoryId){
        var product = productRepository.findByBarcodeAndProductStatus(barcode,ProductStatus.NEW).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Cacheable(value = "products",key = "T(String).valueOf(#barcode).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getImportedProductByBarcodeAndInventoryId(String barcode,Long inventoryId){
        var product = productRepository.findByBarcodeAndProductStatus(barcode,ProductStatus.IMPORTED).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    public Page<ProductDTO> getProductByName(String name,Pageable pageable){
        var products = productRepository.findByNameContainingIgnoreCase(name,pageable);
        return products.map(ProductMapper::toDTO);
    }
    public Page<ProductDTO> getProductsByCategoryName(String name , Pageable pageable){
        var products = productRepository.findByCategoryNameContainingIgnoreCase(name,pageable);
        return products.map(ProductMapper::toDTO);
    }
    @Transactional
    private Product changeProductStatus(Long productId, ProductStatus status , Boolean isAvailable, Boolean isDeleted){
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        Optional.ofNullable(isDeleted).ifPresent(product::setIsDeleted);
        Optional.ofNullable(status).ifPresent(product::setProductStatus);
        Optional.ofNullable(isAvailable).ifPresent(product::setIsAvailable);
        product.setUpdatedAt(LocalDateTime.now());
        if(Boolean.TRUE.equals(isDeleted)){
            product.setDeletedAt(LocalDateTime.now());
        }
        return productRepository.save(product);
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsAvailable(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,true,false));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsUnAvailable(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,false,false));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsNew(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,ProductStatus.NEW,null,null));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsImported(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,ProductStatus.IMPORTED,null,null));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsDeleted(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,false,true));
    }

}
