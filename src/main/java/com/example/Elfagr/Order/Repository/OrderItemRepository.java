package com.example.Elfagr.Order.Repository;

import com.example.Elfagr.Order.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
