package com.example.Elfagr.Security.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendCodeDTO {
    @NotBlank @Email(message = "Please insert valid email ")
    private String email;
}
