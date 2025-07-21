package com.example.Elfagr.Order.Entity;

import com.example.Elfagr.Order.Enum.OrderStatus;
import com.example.Elfagr.Order.Enum.PaymentMethod;
import com.example.Elfagr.User.Entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders",indexes = {
        @Index(name = "idx_customer_name",columnList = "customer_name"),
        @Index(name = "idx_customer_phone",columnList = "customer_phone")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private BigDecimal totalPrice;

    @NotBlank
    private BigDecimal taxAmount;

    @NotBlank
    private BigDecimal discountAmount;

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

    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}
