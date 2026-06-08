package com.example.ptit_cntt2_it211_ss20_ex5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.AuthSession;

import java.util.List;
import java.util.Optional;

public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
    Optional<AuthSession> findByRefreshTokenValue(String token);
    List<AuthSession> findByFanIdAndIsRevokedFalse(Long fanId);
}