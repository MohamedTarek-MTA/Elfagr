package com.example.Elfagr.Order.Repository;

import com.example.Elfagr.Order.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
