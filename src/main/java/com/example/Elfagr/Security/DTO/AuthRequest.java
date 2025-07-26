package com.example.Elfagr.Security.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest implements Serializable {
    @NotBlank @Email(message = "Please insert valid email")
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])" +  // at least one lowercase
                    "(?=.*[A-Z])" +    // at least one uppercase
                    "(?=.*\\d)" +      // at least one digit
                    "(?=.*[@$!%*?&])" +// at least one special char
                    "[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;
}
