package com.example.Elfagr.Order.Repository;

import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.Order.Enum.OrderStatus;
import com.example.Elfagr.Order.Enum.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);

    Page<Order> findByCustomerPhone(String customerPhone,Pageable pageable);

    Page<Order> findByUser_Id(Long userId,Pageable pageable);

    Page<Order> findByPaymentMethod(PaymentMethod paymentMethod,Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus orderStatus,Pageable pageable);

    Page<Order> findByCreatedAtBetween(LocalDateTime start,LocalDateTime end,Pageable pageable);
}
