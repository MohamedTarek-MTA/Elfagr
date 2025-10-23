package com.example.Elfagr.Return.Repository;

import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.Return.Enum.ReturnReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReturnRepository extends JpaRepository<Return,Long> {
    Page<Return> findByOrder_Id(Long orderId,Pageable pageable);
    Page<Return> findByUser_Id(Long userId, Pageable pageable);
    Page<Return> findByCustomerNameContainingIgnoreCase(String customerName,Pageable pageable);
    Page<Return> findByCustomerPhone(String phone,Pageable pageable);
    Page<Return> findByReason(ReturnReason reason,Pageable pageable);
    boolean existsByOrder_Id(Long orderId);
    Page<Return> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
