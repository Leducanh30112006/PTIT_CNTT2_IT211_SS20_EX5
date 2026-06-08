package com.example.ptit_cntt2_it211_ss20_ex5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ptit_cntt2_it211_ss20_ex5.jwt.JwtProvider;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.LoginRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.RefreshRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response.JwtResponse;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.AuthSession;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.FanAccount;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.AuthSessionRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.FanAccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final FanAccountRepository fanAccountRepository;
    private final AuthSessionRepository authSessionRepository;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public JwtResponse login(LoginRequest request) {
        FanAccount fan = fanAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Sai tài khoản hoặc mật khẩu"));

        if (!passwordEncoder.matches(request.getPassword(), fan.getPassword())) {
            throw new RuntimeException("Sai tài khoản hoặc mật khẩu");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(fan.getEmail());
        String accessToken = jwtProvider.generateAccessToken(userDetails);
        String refreshToken = jwtProvider.generateRefreshToken(userDetails);

        AuthSession session = AuthSession.builder()
                .refreshTokenValue(refreshToken)
                .isRevoked(false)
                .isExpired(false)
                .fanId(fan.getId())
                .build();
        authSessionRepository.save(session);

        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse refresh(RefreshRequest request) {
        AuthSession session = authSessionRepository.findByRefreshTokenValue(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token không tồn tại"));

        if (session.isRevoked() || session.isExpired()) {
            throw new RuntimeException("Refresh Token đã hết hạn hoặc bị vô hiệu");
        }

        String email = jwtProvider.extractUsername(request.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtProvider.isTokenValid(request.getRefreshToken(), userDetails)) {
            throw new RuntimeException("Token không hợp lệ");
        }

        String newAccessToken = jwtProvider.generateAccessToken(userDetails);
        return new JwtResponse(newAccessToken, request.getRefreshToken());
    }

    public void logout(String email) {
        FanAccount fan = fanAccountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        List<AuthSession> activeSessions = authSessionRepository.findByFanIdAndIsRevokedFalse(fan.getId());

        activeSessions.stream().forEach(session -> {
            session.setRevoked(true);
            authSessionRepository.save(session);
        });

        SecurityContextHolder.clearContext();
    }
}