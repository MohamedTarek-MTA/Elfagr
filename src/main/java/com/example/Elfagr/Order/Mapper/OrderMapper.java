package com.example.Elfagr.Order.Mapper;

import com.example.Elfagr.Order.DTO.OrderDTO;
import com.example.Elfagr.Order.Entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public static OrderDTO toDTO(Order order){
        return OrderDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerInfo(order.getCustomerInfo())
                .orderStatus(order.getOrderStatus())
                .taxAmount(order.getTaxAmount())
                .discountAmount(order.getDiscountAmount())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .paymentMethod(order.getPaymentMethod())
                .userId(order.getUser().getId())
                .orderItems(order.getOrderItems())
                .updatedAt(order.getUpdatedAt())
                .deletedAt(order.getDeletedAt())
                .isDeleted(order.getIsDeleted())
                .build();
    }
}
