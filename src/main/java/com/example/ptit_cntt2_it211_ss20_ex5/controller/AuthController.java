package com.example.ptit_cntt2_it211_ss20_ex5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.LoginRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.RefreshRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response.JwtResponse;
import com.example.ptit_cntt2_it211_ss20_ex5.service.AuthService;

@RestController
@RequestMapping("/api/concert/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.logout(currentEmail);
        return ResponseEntity.ok("Đăng xuất thành công");
    }
}