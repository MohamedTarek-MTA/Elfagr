package com.example.Elfagr.Inventory.Entity;

import com.example.Elfagr.Inventory.Enum.InventoryType;
import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Order.Entity.OrderItem;
import com.example.Elfagr.Product.Entity.ProductInventory;
import com.example.Elfagr.Return.Entity.ReturnItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventories" , indexes = {
        @Index(name = "idx_inventory_name",columnList = "name")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    @Column(name = "name",nullable = false)
    private String name;

    private String description;

    @NotBlank
    private String address;

    private String contactInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
    @JsonProperty("deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private InventoryType type;

    @OneToMany(mappedBy = "inventory",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<ProductInventory> productInventories;

    @OneToMany(mappedBy = "inventory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<InventoryTransaction> inventoryTransactions;

    @OneToMany(mappedBy = "inventory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "inventory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ReturnItem> returnItems;

}
