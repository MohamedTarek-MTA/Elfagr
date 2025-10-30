package com.example.Elfagr.Security.Controller;

import com.example.Elfagr.Security.Service.CustomUserDetails;
import com.example.Elfagr.Security.Service.CustomUserDetailsService;
import com.example.Elfagr.User.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> adminAPI(@AuthenticationPrincipal CustomUserDetails principal){
        var userDetails = principal.getUser();
        return ResponseEntity.ok(userDetails.getId()+" "+userDetails.getEmail()+"Hello Admin !");
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<?> memberAPI(@AuthenticationPrincipal CustomUserDetails principal){
        var userDetails = principal.getUser();
        return ResponseEntity.ok(userDetails.getId()+" "+userDetails.getEmail()+"Hello Member !");
    }
}
