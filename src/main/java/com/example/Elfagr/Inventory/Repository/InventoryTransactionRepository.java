package com.example.Elfagr.Inventory.Repository;

import com.example.Elfagr.Inventory.Entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction,Long> {

}
