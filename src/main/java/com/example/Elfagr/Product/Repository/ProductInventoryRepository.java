package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInventoryRepository extends JpaRepository<ProductInventory,Long> {
}
