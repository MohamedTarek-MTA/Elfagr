package com.example.Elfagr.Return.Mapper;

import com.example.Elfagr.Return.DTO.ReturnItemDTO;
import com.example.Elfagr.Return.Entity.ReturnItem;
import org.springframework.stereotype.Component;

@Component
public class ReturnItemMapper {
    public static ReturnItemDTO toDTO(ReturnItem returnItem){
        return ReturnItemDTO.builder()
                .id(returnItem.getId())
                .inventoryId(returnItem.getInventory().getId())
                .productId(returnItem.getProduct().getId())
                .quantity(returnItem.getQuantity())
                .subTotal(returnItem.getSubTotal())
                .build();
    }
}
