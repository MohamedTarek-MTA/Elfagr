package com.example.Elfagr.Order.Mapper;

import com.example.Elfagr.Order.DTO.OrderItemDTO;
import com.example.Elfagr.Order.Entity.OrderItem;
import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.Product.Entity.Product;
import com.example.Elfagr.Inventory.Entity.Inventory;
import org.springframework.stereotype.Component;

/**
 * Enhanced OrderItemMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class OrderItemMapper {

    /**
     * Converts OrderItem entity to OrderItemDTO
     * @param orderItem The OrderItem entity to convert
     * @return OrderItemDTO object or null if input is null
     */
    public static OrderItemDTO toDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subTotal(orderItem.getSubTotal())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }

    /**
     * Converts OrderItemDTO to OrderItem entity
     * @param orderItemDTO The OrderItemDTO to convert
     * @param order The order entity to associate
     * @param product The product entity to associate
     * @param inventory The inventory entity to associate
     * @return OrderItem entity or null if input is null
     */
    public static OrderItem toEntity(OrderItemDTO orderItemDTO, Order order, Product product, Inventory inventory) {
        if (orderItemDTO == null) {
            return null;
        }

        return OrderItem.builder()
                .id(orderItemDTO.getId())
                .quantity(orderItemDTO.getQuantity())
                .unitPrice(orderItemDTO.getUnitPrice())
                .subTotal(orderItemDTO.getSubTotal())
                .createdAt(orderItemDTO.getCreatedAt())
                .updatedAt(orderItemDTO.getUpdatedAt())
                .order(order)
                .product(product)
                .inventory(inventory)
                .build();
    }

    /**
     * Converts OrderItemDTO to OrderItem entity for updating existing order items
     * @param orderItemDTO The OrderItemDTO to convert
     * @param existingOrderItem The existing order item entity to update
     * @param product The product entity to associate (optional)
     * @param inventory The inventory entity to associate (optional)
     * @return Updated OrderItem entity or null if input is null
     */
    public static OrderItem toEntityForUpdate(OrderItemDTO orderItemDTO, OrderItem existingOrderItem, 
                                            Product product, Inventory inventory) {
        if (orderItemDTO == null || existingOrderItem == null) {
            return existingOrderItem;
        }

        if (orderItemDTO.getQuantity() != null) {
            existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        }
        if (orderItemDTO.getUnitPrice() != null) {
            existingOrderItem.setUnitPrice(orderItemDTO.getUnitPrice());
        }
        if (orderItemDTO.getSubTotal() != null) {
            existingOrderItem.setSubTotal(orderItemDTO.getSubTotal());
        }
        if (product != null) {
            existingOrderItem.setProduct(product);
        }
        if (inventory != null) {
            existingOrderItem.setInventory(inventory);
        }

        return existingOrderItem;
    }

    /**
     * Creates a simple OrderItem entity for basic operations
     * @param orderItemDTO The OrderItemDTO to convert
     * @return OrderItem entity with minimal fields set
     */
    public static OrderItem toEntitySimple(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null) {
            return null;
        }

        return OrderItem.builder()
                .quantity(orderItemDTO.getQuantity())
                .unitPrice(orderItemDTO.getUnitPrice())
                .subTotal(orderItemDTO.getSubTotal())
                .build();
    }
} 