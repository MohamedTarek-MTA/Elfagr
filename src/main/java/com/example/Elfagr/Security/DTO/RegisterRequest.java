package com.example.Elfagr.Security.DTO;

import com.example.Elfagr.User.Enum.Gender;
import com.example.Elfagr.User.Enum.Role;
import com.example.Elfagr.User.Enum.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank @Email(message = "Please Enter a Valid Email !")
    private String email;
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;
    @NotNull(message = "Role Is Required !")
    private Role role;
    private Status status;
    @NotNull(message = "Gender Is Required !")
    private Gender gender;
    @NotNull(message = "Address Is Required !")
    private String address;
    @NotNull(message = "Birthdate Is Required !")
    private LocalDate birthdate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime creationDate;
    private String verificationCode;
    private boolean isEnabled;
}
