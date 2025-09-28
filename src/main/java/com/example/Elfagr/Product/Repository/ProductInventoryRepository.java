package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.ProductInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
    Optional<ProductInventory> findByProductIdAndInventoryId(Long productId, Long inventoryId);
    Page<ProductInventory> findByProductId(Long productId, Pageable pageable);
}
