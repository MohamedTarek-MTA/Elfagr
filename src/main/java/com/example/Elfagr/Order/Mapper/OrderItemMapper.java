package com.example.Elfagr.Order.Mapper;

import com.example.Elfagr.Order.DTO.OrderItemDTO;
import com.example.Elfagr.Order.Entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderItem orderItem){
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subTotal(orderItem.getSubTotal())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }
}
