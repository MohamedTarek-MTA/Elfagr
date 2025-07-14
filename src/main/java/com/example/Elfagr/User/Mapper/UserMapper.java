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
                user.getCreationDate(),
                user.getLastLoginDate()
        );
    }
    public static User toEntity(UserDTO user,String password , String verificationCode , LocalDateTime deletedAt,Boolean isDeleted){
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail(),
                password,
                user.getImageUrl(),
                user.getGender(),
                user.getRole(),
                user.getPhone(),
                user.getBirthdate(),
                user.getStatus(),
                verificationCode,
                user.getAddress(),
                user.getCreationDate(),
                user.getLastLoginDate(),
                deletedAt,
                user.getIsEnabled(),
                isDeleted
        );

    }
}
