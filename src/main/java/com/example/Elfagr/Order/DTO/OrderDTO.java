package com.example.Elfagr.Order.DTO;

import com.example.Elfagr.Order.Enum.OrderStatus;
import com.example.Elfagr.Order.Enum.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO  implements Serializable {
    private Long id;
    @NotBlank
    private Long userId;

    @DecimalMin(value = "0.0",inclusive = false)
    private BigDecimal totalPrice;


    @DecimalMin(value = "0.0")
    private BigDecimal taxAmount;


    @DecimalMin(value = "0.0")
    private BigDecimal discountAmount;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerPhone;

    private String customerInfo;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
    @NotBlank
    private OrderStatus orderStatus;
    @NotBlank
    private PaymentMethod paymentMethod;
    @NotBlank
    private List<OrderItemDTO> orderItems;
}
