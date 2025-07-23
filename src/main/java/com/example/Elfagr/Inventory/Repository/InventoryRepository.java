package com.example.Elfagr.Inventory.Repository;

import com.example.Elfagr.Inventory.Entity.Inventory;
import com.example.Elfagr.Inventory.Enum.InventoryType;
import com.example.Elfagr.Inventory.Enum.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    Page<Inventory> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Inventory> findByType(InventoryType inventoryType,Pageable pageable);

    Page<Inventory> findByStatus(Status status,Pageable pageable);
}
