package com.example.Elfagr.User.DTO;


import com.example.Elfagr.User.Enum.Gender;
import com.example.Elfagr.User.Enum.Role;
import com.example.Elfagr.User.Enum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDate birthdate;
    private String phone;
    private Status status;
    private Gender gender;
    private String age;
    private String address;
    private String imageUrl;
    private Boolean isEnabled;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime creationDate;
    private LocalDateTime lastLoginDate;
}
