package com.example.ptit_cntt2_it211_ss20_ex5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.FanAccount;

import java.util.Optional;

public interface FanAccountRepository extends JpaRepository<FanAccount, Long> {
    Optional<FanAccount> findByEmail(String email);
}