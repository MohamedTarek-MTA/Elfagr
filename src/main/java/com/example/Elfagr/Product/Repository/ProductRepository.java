package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
