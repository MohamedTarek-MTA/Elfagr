package com.example.Elfagr.Product.DTO;

import com.example.Elfagr.Product.Enum.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO implements Serializable {
    private Long id;
    @NotBlank

    private String name;

    @NotBlank
    private String description;
    @NotBlank
    private CategoryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Boolean isDeleted;
}
