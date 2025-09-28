package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findBySkuOrBarcode(String sku,String barcode);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByCategoryNameIgnoreCase(String categoryName,Pageable pageable);

}
