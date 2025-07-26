package com.example.Elfagr.Security.Service;

import com.example.Elfagr.Mail.DTO.MailDTO;
import com.example.Elfagr.Mail.Mapper.MailMapper;
import com.example.Elfagr.Mail.Service.MailService;
import com.example.Elfagr.Security.DTO.*;
import com.example.Elfagr.Security.Util.JwtUtil;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.Enum.Status;
import com.example.Elfagr.User.Repository.UserRepository;
import com.example.Elfagr.User.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final MailService mailService;
    private final UserRepository userRepository;


    @Transactional
    public String register(RegisterRequest request){
        if(userService.isEmailExists(request.getEmail())){
            throw new IllegalArgumentException("This Email Already Exists !");
        }
        if(userService.isPhoneExists(request.getPhone())){
            throw new IllegalArgumentException("This Phone Number Already Exists !");
        }
        try{
            String code = generateCode();
            User user = User.builder()
                    .name(request.getFirstName()+" "+request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phone(request.getPhone())
                    .birthdate(request.getBirthdate())
                    .role(request.getRole())
                    .gender(request.getGender())
                    .address(request.getAddress())
                    .lastLoginDate(null)
                    .createdAt(LocalDateTime.now())
                    .status(Status.INACTIVE)
                    .isEnabled(false)
                    .isDeleted(false)
                    .deletedAt(null)
                    .imageUrl(null)
                    .verificationCode(code)
                    .build();
            userRepository.save(user);
            var mailRequest = MailMapper.toDTO(user.getEmail(),code);
            mailService.sendCodeToViaEmail(mailRequest);
            return "Please Check Your Email to Get Verification Code !! ";
        }
        catch (Exception e){
            log.error("Error occurred while registering user", e);
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }

    }



    public String verifyEmail(MailDTO dto){

        log.info(String.valueOf(dto.getVerificationCode()));
        log.info(String.valueOf(dto.getEmail()));
        var user = userService.getUserByEmail(dto.getEmail()).orElseThrow(()->new IllegalArgumentException("Incorrect Email !!"));
        if(!user.getVerificationCode().equals(dto.getVerificationCode())){
            log.warn(user.getVerificationCode());
            log.warn(user.getEmail());
            throw new IllegalArgumentException("Code Didn't Match !!");
        }

        user.setVerificationCode(null);
        userService.activeUser(user.getId());
        return "Your Account Has Been Verified Successfully !";
    }

    public AuthResponse login(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
        );
        var user = userService.getUserByEmail(request.getEmail()).orElseThrow(()->new IllegalArgumentException("Invalid email or password !"));
        if(user.getIsDeleted()){
            throw new IllegalArgumentException("This Account Has Been Deleted !");
        }
        if(!user.getStatus().equals(Status.ACTIVE)){
            throw new IllegalArgumentException("Your account is currently " + user.getStatus().name().toLowerCase() + ". Please verify or contact support.");
        }
        String token = jwtUtil.generateToken(user.getId(),user.getEmail(),user.getRole().name(),user.getStatus().name());
        userService.updateLastLoginDate(user.getId());
        return new AuthResponse(token);
    }

    public String resetPassword(ResetPasswordRequest request){
        var user = userService.getUserByEmail(request.getEmail()).orElseThrow(()->new IllegalArgumentException("User Not Found !"));
        if(user.getIsDeleted()){
            throw new IllegalArgumentException("This Account Has Been Deleted !");
        }
        if(!user.getStatus().equals(Status.ACTIVE)){
            throw new IllegalArgumentException("You Couldn't Change Your Current Password Because Your Account Is "+user.getStatus().name());
        }
        if(!request.getPhone().equals(user.getPhone())){
            throw new IllegalArgumentException("Invalid Phone Number !");
        }
        if(!request.getNewPassword().equals(request.getConfirmedNewPassword())){
            throw new IllegalArgumentException("Passwords Don't Match !");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Password Changed Successfully !";
    }


    public String resendVerificationCode(ResendCodeDTO dto){
        var user = userService.getUserByEmail(dto.getEmail()).orElseThrow(()->new IllegalArgumentException("Email Not Found !"));
        try{
            String code = generateCode();
            user.setVerificationCode(code);
            userRepository.save(user);
            var mailRequest = MailMapper.toDTO(user.getEmail(),code);
            mailService.sendCodeToViaEmail(mailRequest);
            return "Please Check Your Email to Get Verification Code !!";
        }
        catch (Exception e){
            throw new RuntimeException("Something Wrong happened please try again ",e.getCause());
        }
    }
    private String generateCode(){
        SecureRandom secureRandom = new SecureRandom();
        int code = 100000 + secureRandom.nextInt(900000); // ensures 6-digit number
        return String.valueOf(code);
    }
}
