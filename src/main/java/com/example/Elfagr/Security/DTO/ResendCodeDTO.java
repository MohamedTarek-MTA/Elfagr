package com.example.Elfagr.Security.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendCodeDTO  implements Serializable{
    @NotBlank @Email(message = "Please insert valid email ")
    private String email;
}
