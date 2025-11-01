package com.example.Elfagr.Return.DTO;

import com.example.Elfagr.Return.Entity.ReturnItem;
import com.example.Elfagr.Return.Enum.ReturnReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ReturnDTO implements Serializable {
    private Long id;
    @NotBlank
    private Long orderId;
    @NotBlank
    private Long userId;
    @NotNull
    private ReturnReason reason;

    private String notes;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    @Column(name = "customer_name")
    private String customerName;

    @NotBlank
    @Column(name = "customer_phone")
    private String customerPhone;

    private String customerInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
    private List<ReturnItemDTO> returnItems;
}
