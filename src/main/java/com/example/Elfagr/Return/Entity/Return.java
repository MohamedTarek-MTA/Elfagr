package com.example.Elfagr.Return.Entity;

import com.example.Elfagr.Order.Entity.Order;
import com.example.Elfagr.User.Entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "returns",indexes = {
        @Index(name = "idx_customer_name",columnList = "customer_name"),
        @Index(name = "idx_customer_phone",columnList = "customer_phone")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Return {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String reason;

    @NotBlank
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

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "aReturn",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ReturnItem> returnItems;
}
