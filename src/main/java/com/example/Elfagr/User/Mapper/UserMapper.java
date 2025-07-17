package com.example.Elfagr.User.Mapper;

import com.example.Elfagr.Shared.Service.CalculateAgeService;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.DTO.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public static UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getBirthdate(),
                user.getPhone(),
                user.getStatus(),
                user.getGender(),
                CalculateAgeService.getAge(user.getBirthdate()),
                user.getAddress(),
                user.getImageUrl(),
                user.getIsEnabled(),
                user.getIsDeleted(),
                user.getDeletedAt(),
                user.getCreatedAt(),
                user.getLastLoginDate()
        );
    }
    public static User toEntity(UserDTO user,String password , String verificationCode , LocalDateTime deletedAt,Boolean isDeleted){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(password)
                .address(user.getAddress())
                .birthdate(user.getBirthdate())
                .phone(user.getPhone())
                .role(user.getRole())
                .gender(user.getGender())
                .imageUrl(user.getImageUrl())
                .status(user.getStatus())
                .isEnabled(user.getIsEnabled())
                .isDeleted(user.getIsDeleted())
                .createdAt(user.getCreatedAt())
                .lastLoginDate(user.getLastLoginDate())
                .deletedAt(user.getDeletedAt())
                .verificationCode(verificationCode)
                .build();

    }
}
