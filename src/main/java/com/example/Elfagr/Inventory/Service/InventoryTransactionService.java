package com.example.Elfagr.Inventory.Service;

import com.example.Elfagr.Inventory.DTO.InventoryTransactionDTO;
import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import com.example.Elfagr.Inventory.Mapper.InventoryTransactionMapper;
import com.example.Elfagr.Inventory.Repository.InventoryRepository;
import com.example.Elfagr.Inventory.Repository.InventoryTransactionRepository;
import com.example.Elfagr.Product.Repository.ProductRepository;
import com.example.Elfagr.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryTransactionService {
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "inventoryTransactions",key = "#id")
    public InventoryTransactionDTO getTransactionById(Long id){
        var transaction = inventoryTransactionRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Not Found !"));

        return InventoryTransactionMapper.toDTO(transaction);
    }

    public Page<InventoryTransactionDTO> getTransactionsByInventoryId(Long inventoryId, Pageable pageable){
        var transactions = inventoryTransactionRepository.findByInventory_Id(inventoryId,pageable);
        return transactions.map(InventoryTransactionMapper::toDTO);
    }

    public Page<InventoryTransactionDTO> getTransactionsByProductId(Long productId,Pageable pageable){
        var transactions = inventoryTransactionRepository.findByProduct_Id(productId,pageable);
        return transactions.map(InventoryTransactionMapper::toDTO);
    }

    public Page<InventoryTransactionDTO> getTransactionsMadeByEmployee(Long employeeId,Pageable pageable){
        var transactions = inventoryTransactionRepository.findByUser_Id(employeeId,pageable);
        return transactions.map(InventoryTransactionMapper::toDTO);
    }
    public Page<InventoryTransactionDTO> getTransactionsByReason(TransactionReason reason,Pageable pageable){
        var transactions = inventoryTransactionRepository.findByReason(reason,pageable);
        return transactions.map(InventoryTransactionMapper::toDTO);
    }

    public Page<InventoryTransactionDTO> getTransactionsByType(TransactionType type,Pageable pageable){
        var transactions = inventoryTransactionRepository.findByType(type,pageable);
        return transactions.map(InventoryTransactionMapper::toDTO);
    }

    public Page<InventoryTransactionDTO> getInTransactions(Pageable pageable){
        return getTransactionsByType(TransactionType.IN,pageable);
    }
    public Page<InventoryTransactionDTO> getOutTransactions(Pageable pageable){
        return getTransactionsByType(TransactionType.OUT,pageable);
    }

    public Page<InventoryTransactionDTO> getSoledTransactions(Pageable pageable){
        return getTransactionsByReason(TransactionReason.SOLD,pageable);
    }
    public Page<InventoryTransactionDTO> getDamagedTransactions(Pageable pageable){
        return getTransactionsByReason(TransactionReason.DAMAGED,pageable);
    }
    public Page<InventoryTransactionDTO> getReturnedTransactions(Pageable pageable){
        return getTransactionsByReason(TransactionReason.RETURNED,pageable);
    }
    public Page<InventoryTransactionDTO> getRestockedTransactions(Pageable pageable){
        return getTransactionsByReason(TransactionReason.RESTOCKED,pageable);
    }
    public Page<InventoryTransactionDTO> getAllTransactions(Pageable pageable){
        return inventoryTransactionRepository.findAll(pageable).map(InventoryTransactionMapper::toDTO);
    }
    @Transactional
    public void  createInventoryTransaction(Long userId,InventoryTransactionDTO dto){
        var user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        var inventory = inventoryRepository.findById(dto.getInventoryId()).orElseThrow(()->new IllegalArgumentException("Inventory Not Found !"));
        var product = productRepository.findById(dto.getProductId()).orElseThrow(()->new IllegalArgumentException("Product Not Found !"));
        InventoryTransaction.builder()
                .inventory(inventory)
                .product(product)
                .reason(dto.getReason())
                .type(dto.getType())
                .quantityChange(dto.getQuantityChange())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }
}
