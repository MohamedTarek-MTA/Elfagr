package com.example.Elfagr.Product.Repository;

import com.example.Elfagr.Product.Entity.Category;
import com.example.Elfagr.Product.Enum.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByNameContainingIgnoreCase(String name);

    Page<Category> findByStatus(CategoryStatus status,Pageable pageable);


}
