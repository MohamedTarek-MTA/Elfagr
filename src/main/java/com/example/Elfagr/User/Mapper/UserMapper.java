package com.example.Elfagr.User.Mapper;

import com.example.Elfagr.Shared.Service.CalculateAgeService;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.DTO.UserDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Enhanced UserMapper with comprehensive mapping capabilities
 * Handles both Entity to DTO and DTO to Entity conversions
 * Includes null safety and validation
 */
@Component
public class UserMapper {

    /**
     * Converts User entity to UserDTO
     * @param user The User entity to convert
     * @return UserDTO object or null if input is null
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .birthdate(user.getBirthdate())
                .phone(user.getPhone())
                .status(user.getStatus())
                .gender(user.getGender())
                .age(user.getBirthdate() != null ? CalculateAgeService.getAge(user.getBirthdate()) : null)
                .address(user.getAddress())
                .imageUrl(user.getImageUrl())
                .isEnabled(user.getIsEnabled())
                .isDeleted(user.getIsDeleted())
                .deletedAt(user.getDeletedAt())
                .createdAt(user.getCreatedAt())
                .lastLoginDate(user.getLastLoginDate())
                .build();
    }

    /**
     * Converts UserDTO to User entity for creating new users
     * @param userDTO The UserDTO to convert
     * @param password The encoded password
     * @param verificationCode The verification code
     * @return User entity or null if input is null
     */
    public static User toEntity(UserDTO userDTO, String password, String verificationCode) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(password)
                .address(userDTO.getAddress())
                .birthdate(userDTO.getBirthdate())
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .gender(userDTO.getGender())
                .imageUrl(userDTO.getImageUrl())
                .status(userDTO.getStatus())
                .isEnabled(userDTO.getIsEnabled() != null ? userDTO.getIsEnabled() : false)
                .isDeleted(userDTO.getIsDeleted() != null ? userDTO.getIsDeleted() : false)
                .createdAt(userDTO.getCreatedAt())
                .lastLoginDate(userDTO.getLastLoginDate())
                .deletedAt(userDTO.getDeletedAt())
                .verificationCode(verificationCode)
                .build();
    }

    /**
     * Converts UserDTO to User entity for updating existing users
     * @param userDTO The UserDTO to convert
     * @param existingUser The existing user entity to update
     * @return Updated User entity or null if input is null
     */
    public static User toEntityForUpdate(UserDTO userDTO, User existingUser) {
        if (userDTO == null || existingUser == null) {
            return existingUser;
        }

        if (userDTO.getName() != null) {
            existingUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getBirthdate() != null) {
            existingUser.setBirthdate(userDTO.getBirthdate());
        }
        if (userDTO.getPhone() != null) {
            existingUser.setPhone(userDTO.getPhone());
        }
        if (userDTO.getStatus() != null) {
            existingUser.setStatus(userDTO.getStatus());
        }
        if (userDTO.getGender() != null) {
            existingUser.setGender(userDTO.getGender());
        }
        if (userDTO.getAddress() != null) {
            existingUser.setAddress(userDTO.getAddress());
        }
        if (userDTO.getImageUrl() != null) {
            existingUser.setImageUrl(userDTO.getImageUrl());
        }
        if (userDTO.getRole() != null) {
            existingUser.setRole(userDTO.getRole());
        }
        if (userDTO.getIsEnabled() != null) {
            existingUser.setIsEnabled(userDTO.getIsEnabled());
        }

        return existingUser;
    }

    /**
     * Creates a simple User entity for basic operations
     * @param userDTO The UserDTO to convert
     * @return User entity with minimal fields set
     */
    public static User toEntitySimple(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .birthdate(userDTO.getBirthdate())
                .phone(userDTO.getPhone())
                .role(userDTO.getRole())
                .gender(userDTO.getGender())
                .address(userDTO.getAddress())
                .status(userDTO.getStatus())
                .imageUrl(userDTO.getImageUrl())
                .isEnabled(false)
                .isDeleted(false)
                .build();
    }
} 