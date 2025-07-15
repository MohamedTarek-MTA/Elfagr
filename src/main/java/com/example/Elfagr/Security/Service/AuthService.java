package com.example.Elfagr.Security.Service;

import com.example.Elfagr.Mail.DTO.MailDTO;
import com.example.Elfagr.Mail.Mapper.MailMapper;
import com.example.Elfagr.Mail.Service.MailService;
import com.example.Elfagr.Security.DTO.AuthRequest;
import com.example.Elfagr.Security.DTO.AuthResponse;
import com.example.Elfagr.Security.DTO.RegisterRequest;
import com.example.Elfagr.Security.DTO.ResetPasswordRequest;
import com.example.Elfagr.Security.Util.JwtUtil;
import com.example.Elfagr.User.Entity.User;
import com.example.Elfagr.User.Enum.Status;
import com.example.Elfagr.User.Repository.UserRepository;
import com.example.Elfagr.User.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

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
        var user = new User();
        user.setName(request.getFirstName()+" "+request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBirthdate(request.getBirthdate());
        user.setRole(request.getRole());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setLastLoginDate(null);
        user.setCreationDate(LocalDateTime.now());
        user.setStatus(Status.INACTIVE);
        user.setIsEnabled(false);
        user.setIsDeleted(false);
        user.setImageUrl(null);
        String code = String.valueOf(100000 + new Random().nextInt(999999));
        user.setVerificationCode(code);
        log.info(String.valueOf(user));
        userRepository.save(user);
        var mailRequest = MailMapper.toDTO(user.getEmail(),code);
        mailService.sendCodeToViaEmail(mailRequest);
        return "Please Check Your Email to Get Verification Code !! ";
    }

    public String verifyEmail(MailDTO dto){

        var user = userService.getUserByEmail(dto.getEmail()).orElseThrow(()->new IllegalArgumentException("Incorrect Email !!"));
        if(!user.getVerificationCode().equals(dto.getVerificationCode())){
            throw new IllegalArgumentException("Code Didn't Match !!");
        }
        user.setIsEnabled(true);
        user.setVerificationCode(null);
        user.setStatus(Status.ACTIVE);
        log.info(String.valueOf(user));
        userRepository.save(user);
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
            throw new IllegalArgumentException("This Account Can't Login Because it's "+user.getStatus().name());
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
        log.info(request.getNewPassword());
        userRepository.save(user);
        return "Password Changed Successfully !";
    }
}
