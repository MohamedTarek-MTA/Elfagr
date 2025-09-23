package com.example.Elfagr.Mail.Mapper;

import com.example.Elfagr.Mail.DTO.MailDTO;
import org.springframework.stereotype.Component;

/**
 * Enhanced MailMapper with comprehensive mapping capabilities
 * Handles mail-related data transformations
 * Includes null safety and validation
 */
@Component
public class MailMapper {

    /**
     * Creates a MailDTO from email and verification code
     * @param email The email address
     * @param verificationCode The verification code
     * @return MailDTO object or null if inputs are invalid
     */
    public static MailDTO toDTO(String email, String verificationCode) {
        if (email == null || email.trim().isEmpty() || 
            verificationCode == null || verificationCode.trim().isEmpty()) {
            return null;
        }

        return MailDTO.builder()
                .email(email.trim())
                .verificationCode(verificationCode.trim())
                .build();
    }

    /**
     * Creates a MailDTO from another MailDTO (copy constructor)
     * @param mailDTO The source MailDTO
     * @return New MailDTO object or null if input is null
     */
    public static MailDTO toDTO(MailDTO mailDTO) {
        if (mailDTO == null) {
            return null;
        }

        return MailDTO.builder()
                .email(mailDTO.getEmail())
                .verificationCode(mailDTO.getVerificationCode())
                .build();
    }

    /**
     * Validates and creates a MailDTO with email validation
     * @param email The email address
     * @param verificationCode The verification code
     * @return MailDTO object or null if email format is invalid
     */
    public static MailDTO toDTOWithValidation(String email, String verificationCode) {
        if (email == null || verificationCode == null) {
            return null;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            return null;
        }

        return toDTO(email, verificationCode);
    }

    /**
     * Extracts email from MailDTO
     * @param mailDTO The MailDTO to extract from
     * @return Email string or null if input is null
     */
    public static String extractEmail(MailDTO mailDTO) {
        return mailDTO != null ? mailDTO.getEmail() : null;
    }

    /**
     * Extracts verification code from MailDTO
     * @param mailDTO The MailDTO to extract from
     * @return Verification code string or null if input is null
     */
    public static String extractVerificationCode(MailDTO mailDTO) {
        return mailDTO != null ? mailDTO.getVerificationCode() : null;
    }
} 