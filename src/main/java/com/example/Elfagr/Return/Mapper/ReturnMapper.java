package com.example.Elfagr.Return.Mapper;

import com.example.Elfagr.Return.DTO.ReturnDTO;
import com.example.Elfagr.Return.Entity.Return;
import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.User.Entity.User;
import org.springframework.stereotype.Component;

/**
 * Enhanced ReturnMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class ReturnMapper {

    /**
     * Converts Return entity to ReturnDTO
     * @param aReturn The Return entity to convert
     * @return ReturnDTO object or null if input is null
     */
    public static ReturnDTO toDTO(Return aReturn) {
        if (aReturn == null) {
            return null;
        }

        return ReturnDTO.builder()
                .id(aReturn.getId())
                .orderId(aReturn.getOrder() != null ? aReturn.getOrder().getId() : null)
                .userId(aReturn.getUser() != null ? aReturn.getUser().getId() : null)
                .totalAmount(aReturn.getTotalAmount())
                .customerName(aReturn.getCustomerName())
                .customerPhone(aReturn.getCustomerPhone())
                .customerInfo(aReturn.getCustomerInfo())
                .reason(aReturn.getReason())
                .returnItems(aReturn.getReturnItems())
                .createdAt(aReturn.getCreatedAt())
                .isDeleted(aReturn.getIsDeleted())
                .deletedAt(aReturn.getDeletedAt())
                .updatedAt(aReturn.getUpdatedAt())
                .build();
    }

    /**
     * Converts ReturnDTO to Return entity
     * @param returnDTO The ReturnDTO to convert
     * @param order The order entity to associate
     * @param user The user entity to associate
     * @return Return entity or null if input is null
     */
    public static Return toEntity(ReturnDTO returnDTO, Order order, User user) {
        if (returnDTO == null) {
            return null;
        }

        return Return.builder()
                .id(returnDTO.getId())
                .reason(returnDTO.getReason())
                .totalAmount(returnDTO.getTotalAmount())
                .customerName(returnDTO.getCustomerName())
                .customerPhone(returnDTO.getCustomerPhone())
                .customerInfo(returnDTO.getCustomerInfo())
                .createdAt(returnDTO.getCreatedAt())
                .updatedAt(returnDTO.getUpdatedAt())
                .deletedAt(returnDTO.getDeletedAt())
                .isDeleted(returnDTO.getIsDeleted() != null ? returnDTO.getIsDeleted() : false)
                .order(order)
                .user(user)
                .build();
    }

    /**
     * Converts ReturnDTO to Return entity for updating existing returns
     * @param returnDTO The ReturnDTO to convert
     * @param existingReturn The existing return entity to update
     * @param order The order entity to associate (optional)
     * @param user The user entity to associate (optional)
     * @return Updated Return entity or null if input is null
     */
    public static Return toEntityForUpdate(ReturnDTO returnDTO, Return existingReturn, Order order, User user) {
        if (returnDTO == null || existingReturn == null) {
            return existingReturn;
        }

        if (returnDTO.getReason() != null) {
            existingReturn.setReason(returnDTO.getReason());
        }
        if (returnDTO.getTotalAmount() != null) {
            existingReturn.setTotalAmount(returnDTO.getTotalAmount());
        }
        if (returnDTO.getCustomerName() != null) {
            existingReturn.setCustomerName(returnDTO.getCustomerName());
        }
        if (returnDTO.getCustomerPhone() != null) {
            existingReturn.setCustomerPhone(returnDTO.getCustomerPhone());
        }
        if (returnDTO.getCustomerInfo() != null) {
            existingReturn.setCustomerInfo(returnDTO.getCustomerInfo());
        }
        if (order != null) {
            existingReturn.setOrder(order);
        }
        if (user != null) {
            existingReturn.setUser(user);
        }

        return existingReturn;
    }

    /**
     * Creates a simple Return entity for basic operations
     * @param returnDTO The ReturnDTO to convert
     * @return Return entity with minimal fields set
     */
    public static Return toEntitySimple(ReturnDTO returnDTO) {
        if (returnDTO == null) {
            return null;
        }

        return Return.builder()
                .reason(returnDTO.getReason())
                .totalAmount(returnDTO.getTotalAmount())
                .customerName(returnDTO.getCustomerName())
                .customerPhone(returnDTO.getCustomerPhone())
                .customerInfo(returnDTO.getCustomerInfo())
                .isDeleted(false)
                .build();
    }
} 