package com.example.Elfagr.Order.Mapper;

import com.example.Elfagr.Order.DTO.OrderDTO;
import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.User.Entity.User;
import org.springframework.stereotype.Component;

/**
 * Enhanced OrderMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class OrderMapper {

    /**
     * Converts Order entity to OrderDTO
     * @param order The Order entity to convert
     * @return OrderDTO object or null if input is null
     */
    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

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
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .orderItems(order.getOrderItems().stream().map(OrderItemMapper::toDTO).toList())
                .notes(order.getNotes())
                .updatedAt(order.getUpdatedAt())
                .deletedAt(order.getDeletedAt())
                .isDeleted(order.getIsDeleted())
                .build();
    }

    /**
     * Converts OrderDTO to Order entity
     * @param orderDTO The OrderDTO to convert
     * @param user The user entity to associate with the order
     * @return Order entity or null if input is null
     */
    public static Order toEntity(OrderDTO orderDTO, User user) {
        if (orderDTO == null) {
            return null;
        }

        return Order.builder()
                .id(orderDTO.getId())
                .totalPrice(orderDTO.getTotalPrice())
                .taxAmount(orderDTO.getTaxAmount())
                .discountAmount(orderDTO.getDiscountAmount())
                .customerName(orderDTO.getCustomerName())
                .customerPhone(orderDTO.getCustomerPhone())
                .customerInfo(orderDTO.getCustomerInfo())
                .notes(orderDTO.getNotes())
                .createdAt(orderDTO.getCreatedAt())
                .updatedAt(orderDTO.getUpdatedAt())
                .deletedAt(orderDTO.getDeletedAt())
                .isDeleted(orderDTO.getIsDeleted() != null ? orderDTO.getIsDeleted() : false)
                .orderStatus(orderDTO.getOrderStatus())
                .paymentMethod(orderDTO.getPaymentMethod())
                .user(user)
                .build();
    }

    /**
     * Converts OrderDTO to Order entity for updating existing orders
     * @param orderDTO The OrderDTO to convert
     * @param existingOrder The existing order entity to update
     * @param user The user entity to associate (optional)
     * @return Updated Order entity or null if input is null
     */
    public static Order toEntityForUpdate(OrderDTO orderDTO, Order existingOrder, User user) {
        if (orderDTO == null || existingOrder == null) {
            return existingOrder;
        }

        if (orderDTO.getTotalPrice() != null) {
            existingOrder.setTotalPrice(orderDTO.getTotalPrice());
        }
        if (orderDTO.getTaxAmount() != null) {
            existingOrder.setTaxAmount(orderDTO.getTaxAmount());
        }
        if (orderDTO.getDiscountAmount() != null) {
            existingOrder.setDiscountAmount(orderDTO.getDiscountAmount());
        }
        if (orderDTO.getCustomerName() != null) {
            existingOrder.setCustomerName(orderDTO.getCustomerName());
        }
        if (orderDTO.getCustomerPhone() != null) {
            existingOrder.setCustomerPhone(orderDTO.getCustomerPhone());
        }
        if (orderDTO.getCustomerInfo() != null) {
            existingOrder.setCustomerInfo(orderDTO.getCustomerInfo());
        }
        if (orderDTO.getOrderStatus() != null) {
            existingOrder.setOrderStatus(orderDTO.getOrderStatus());
        }
        if (orderDTO.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(orderDTO.getPaymentMethod());
        }
        if (user != null) {
            existingOrder.setUser(user);
        }

        return existingOrder;
    }

    /**
     * Creates a simple Order entity for basic operations
     * @param orderDTO The OrderDTO to convert
     * @return Order entity with minimal fields set
     */
    public static Order toEntitySimple(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        return Order.builder()
                .totalPrice(orderDTO.getTotalPrice())
                .taxAmount(orderDTO.getTaxAmount())
                .discountAmount(orderDTO.getDiscountAmount())
                .customerName(orderDTO.getCustomerName())
                .customerPhone(orderDTO.getCustomerPhone())
                .customerInfo(orderDTO.getCustomerInfo())
                .orderStatus(orderDTO.getOrderStatus())
                .paymentMethod(orderDTO.getPaymentMethod())
                .isDeleted(false)
                .build();
    }
} 