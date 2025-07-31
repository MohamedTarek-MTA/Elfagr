package com.example.Elfagr.Return.Mapper;

import com.example.Elfagr.Return.DTO.ReturnDTO;
import com.example.Elfagr.Return.Entity.Return;
import org.springframework.stereotype.Component;

@Component
public class ReturnMapper {
    public static ReturnDTO toDTO(Return aReturn){
        return ReturnDTO.builder()
                .id(aReturn.getId())
                .orderId(aReturn.getOrder().getId())
                .userId(aReturn.getUser().getId())
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
}
