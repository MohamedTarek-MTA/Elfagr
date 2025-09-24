package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
    Optional<ProductInventory> findByProductIdAndInventoryId(Long productId, Long inventoryId);
}
