package com.example.Elfagr.Return.Repository;

import com.example.Elfagr.Return.Entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReturnRepository extends JpaRepository<Return,Long> {
    Optional<Return> findByOrder_Id(Long orderId);
    boolean existsByOrder_Id(Long orderId);
}
