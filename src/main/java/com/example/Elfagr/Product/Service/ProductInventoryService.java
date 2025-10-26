package com.example.Elfagr.Product.Service;

import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Inventory.Mapper.InventoryTransactionMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Inventory.Service.InventoryTransactionService;
import com.example.Elfagr.Product.DTO.ProductInventoryDTO;
import com.example.Elfagr.Product.Entity.ProductInventory;
import com.example.Elfagr.Product.Mapper.ProductInventoryMapper;
import com.example.Elfagr.Product.Repository.ProductInventoryRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductInventoryService {
    private final UserRepository userRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionService inventoryTransactionService;
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
        inventoryTransactionService.createInventoryTransaction(userId, InventoryTransactionMapper.toDTO(oldInventoryTransaction));
        inventoryTransactionService.createInventoryTransaction(userId,InventoryTransactionMapper.toDTO(newInventoryTransaction));
        oldProductInventory.setIsAvailable(false);
        oldProductInventory.setIsDeleted(true);
        oldProductInventory.setQuantity(0);
        oldProductInventory.setDeletedAt(LocalDateTime.now());
        productInventoryRepository.save(oldProductInventory);
        return ProductInventoryMapper.toDTO(newProductInventory);
    }

    @Transactional
    public ProductInventoryDTO changeProductStockQuantity(Long userId,Long productId,Long inventoryId,Integer quantity,TransactionType transactionType,TransactionReason reason){
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
        return ProductInventoryMapper.toDTO(productInventory);
    }
}
