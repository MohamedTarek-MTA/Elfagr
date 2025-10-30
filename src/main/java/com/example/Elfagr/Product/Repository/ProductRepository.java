package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Product.Enum.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findBySkuAndBarcodeAndProductStatus(String sku,String barcode,ProductStatus status);

    Optional<Product> findBySkuAndProductStatus(String sku, ProductStatus status);

    Optional<Product> findByBarcodeAndProductStatus(String barcode,ProductStatus status);
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Product> findByCategoryNameContainingIgnoreCase(String categoryName,Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name,Pageable pageable);

}
