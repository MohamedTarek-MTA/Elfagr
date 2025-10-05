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
        if(!inventory.getStatus().equals(Status.ACTIVE)){
            throw new IllegalArgumentException("Sorry This Inventory Is Not Working Please Try Again Later !");
        }
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
    public String changeProductStockQuantity(Long userId,Long productId,Long inventoryId,Integer quantity,TransactionType transactionType,TransactionReason reason){
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(productId,inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found in this Inventory !"));
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var inventory = inventoryRepository.findById(inventoryId).orElseThrow(()->new IllegalArgumentException("Inventory Not Found !"));
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        if(transactionType.equals(TransactionType.IN)){
            productInventory.setQuantity(productInventory.getQuantity()+quantity);
        }
        if (transactionType.equals(TransactionType.OUT)) {
           if(productInventory.getQuantity()-quantity < 0)
               throw new IllegalArgumentException("Stock Quantity Could Not be a negative value !");
           productInventory.setQuantity(productInventory.getQuantity()-quantity);
        }
        productInventoryRepository.save(productInventory);
        var inventoryTransaction = InventoryTransaction.builder()
                .user(user)
                .product(product)
                .inventory(inventory)
                .quantityChange(quantity)
                .reason(reason)
                .type(transactionType)
                .createdAt(LocalDateTime.now())
                .build();
        inventoryTransactionService.createInventoryTransaction(userId,InventoryTransactionMapper.toDTO(inventoryTransaction));
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
    public Page<ProductDTO> getAllProductsByInventoryId(Long inventoryId,Pageable pageable){
        var inventory = inventoryRepository.findById(inventoryId).orElseThrow(()->new IllegalArgumentException("Inventory Not Found !"));
        var productInventories = productInventoryRepository.findByInventoryId(inventoryId,pageable);
        return productInventories.map(product ->
                ProductMapper.toDTO(
                        productRepository.findById(
                            product.getId()).orElseThrow(()->
                                new IllegalArgumentException("Product Not Found !"))));
    }
    @Cacheable(value = "products",key = "T(String).valueOf(#sku).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getProductBySkuAndInventoryId(String sku,Long inventoryId){
        var product = productRepository.findBySkuOrBarcode(sku,null).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    @Cacheable(value = "products",key = "T(String).valueOf(#barcode).concat('-').concat(T(String).valueOf(#inventoryId))")
    public ProductDTO getProductByBarcodeAndInventoryId(String barcode,Long inventoryId){
        var product = productRepository.findBySkuOrBarcode(null,barcode).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var productInventory = productInventoryRepository.findByProductIdAndInventoryId(product.getId(),inventoryId).orElseThrow(()->new IllegalArgumentException("Product Not Found In this Inventory !"));
        var productDTO = ProductMapper.toDTO(product,product.getCategory().getId(),inventoryId);
        productDTO.setStockQuantity(productInventory.getQuantity());
        return productDTO;
    }
    public Page<ProductDTO> getProductByName(String name,Pageable pageable){
        var products = productRepository.findByNameIgnoreCase(name,pageable);
        return products.map(ProductMapper::toDTO);
    }
    public Page<ProductDTO> getProductsByCategoryName(String name , Pageable pageable){
        var products = productRepository.findByCategoryNameIgnoreCase(name,pageable);
        return products.map(ProductMapper::toDTO);
    }
    @Transactional
    public Product changeProductStatus(Long productId, ProductStatus status , Boolean isAvailable,Boolean isDeleted){
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        Optional.ofNullable(isDeleted).ifPresent(product::setIsDeleted);
        Optional.ofNullable(status).ifPresent(product::setStatus);
        Optional.ofNullable(isAvailable).ifPresent(product::setIsAvailable);
        product.setUpdatedAt(LocalDateTime.now());
        if(Boolean.TRUE.equals(isDeleted)){
            product.setDeletedAt(LocalDateTime.now());
        }
        return productRepository.save(product);
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsAvailable(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,true,null));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsInAvailable(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,false,null));
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
        return ProductMapper.toDTO(changeProductStatus(productId,null,null,true));
    }
    @CachePut(value = "products",key = "#productId")
    public ProductDTO markProductAsNotDeleted(Long productId){
        return ProductMapper.toDTO(changeProductStatus(productId,null,null,false));
    }
    @Transactional
    @CachePut(value = "productInventories",key = "#productId")
    public ProductInventoryDTO changeProductInventory(Long userId, Long productId, Long oldInventoryId, Long newInventoryId, TransactionReason reason){
        if(oldInventoryId.equals(newInventoryId)){
            throw new IllegalArgumentException("Product Is Already in the same Inventory !");
        }
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var oldProductInventory = productInventoryRepository.findByProductIdAndInventoryId(productId,oldInventoryId).orElseThrow(()->new IllegalArgumentException("Product Is Not Stocked In This Inventory !"));
        var product = productRepository.findById(productId).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        var oldInventory = inventoryRepository.findById(oldInventoryId).orElseThrow(()->new IllegalArgumentException("Old Inventory Not Found !"));
        var newInventory = inventoryRepository.findById(newInventoryId).orElseThrow(()->new IllegalArgumentException("New Inventory Not Found !"));
        var newProductInventory = ProductInventory.builder()
                .product(product)
                .inventory(newInventory)
                .isAvailable(oldProductInventory.getIsAvailable())
                .quantity(oldProductInventory.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();
        productInventoryRepository.save(newProductInventory);
        var oldInventoryTransaction = InventoryTransaction.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .product(product)
                .inventory(oldInventory)
                .type(TransactionType.OUT)
                .quantityChange(oldProductInventory.getQuantity())
                .reason(reason)
                .build();
        var newInventoryTransaction = InventoryTransaction.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .product(product)
                .inventory(newInventory)
                .type(TransactionType.IN)
                .quantityChange(newProductInventory.getQuantity())
                .reason(reason)
                .build();
        inventoryTransactionService.createInventoryTransaction(userId,InventoryTransactionMapper.toDTO(oldInventoryTransaction));
        inventoryTransactionService.createInventoryTransaction(userId,InventoryTransactionMapper.toDTO(newInventoryTransaction));
        oldProductInventory.setIsAvailable(false);
        oldProductInventory.setIsDeleted(true);
        oldProductInventory.setQuantity(0);
        oldProductInventory.setDeletedAt(LocalDateTime.now());
        productInventoryRepository.save(oldProductInventory);
        return ProductInventoryMapper.toDTO(newProductInventory);
    }
}
