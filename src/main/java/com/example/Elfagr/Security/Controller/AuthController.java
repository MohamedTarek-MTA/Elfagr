package com.example.Elfagr.Security.Controller;

import com.example.Elfagr.Mail.DTO.MailDTO;
import com.example.Elfagr.Security.DTO.AuthRequest;
import com.example.Elfagr.Security.DTO.RegisterRequest;
import com.example.Elfagr.Security.DTO.ResetPasswordRequest;
import com.example.Elfagr.Security.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody MailDTO dto){
        return ResponseEntity.ok(authService.verifyEmail(dto));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(authService.resetPassword(request));
    }
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCode(@RequestBody String email){
        return ResponseEntity.ok(authService.resendVerificationCode(email));
    }
    @GetMapping("/say-hello")
    public ResponseEntity<?> sayHello(){
        return ResponseEntity.ok("Hello !");
    }
}
