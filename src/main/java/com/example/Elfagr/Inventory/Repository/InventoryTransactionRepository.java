package com.example.Elfagr.Inventory.Repository;

import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import com.example.Elfagr.Inventory.Enum.TransactionReason;
import com.example.Elfagr.Inventory.Enum.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction,Long> {
    Page<InventoryTransaction> findByInventory_Id(Long inventoryId, Pageable pageable);
    Page<InventoryTransaction> findByProduct_Id(Long productId,Pageable pageable);
    Page<InventoryTransaction> findByUser_Id(Long userId,Pageable pageable);

    Page<InventoryTransaction> findByType(TransactionType type,Pageable pageable);

    Page<InventoryTransaction> findByReason(TransactionReason reason,Pageable pageable);
}
