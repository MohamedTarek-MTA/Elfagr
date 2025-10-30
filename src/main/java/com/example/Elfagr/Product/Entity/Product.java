package com.example.Elfagr.Product.Entity;

import com.example.Elfagr.Order.Entity.OrderItem;
import com.example.Elfagr.Product.Enum.ProductStatus;
import com.example.Elfagr.Return.Entity.ReturnItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_product_sku", columnList = "sku"),
                @Index(name = "idx_product_barcode", columnList = "barcode")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"barcode", "sku", "product_status"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(name = "sku")
    private String sku;

    @NotBlank
    @Column(name = "barcode")
    private String barcode;

    private String description;

    @NotNull
    private BigDecimal price;

    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;
    @JsonProperty("available")
    private Boolean isAvailable;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_status")
    private ProductStatus productStatus;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<ProductInventory> productInventories;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ReturnItem> returnItems;
}
