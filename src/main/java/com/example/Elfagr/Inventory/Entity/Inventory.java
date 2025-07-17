package com.example.Elfagr.Inventory.Entity;

import com.example.Elfagr.Inventory.Enum.Status;
import com.example.Elfagr.Inventory.Enum.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

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

    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Type type;

}
