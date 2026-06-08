package com.example.ptit_cntt2_it211_ss20_ex5.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String refreshTokenValue;
    private boolean isRevoked;
    private boolean isExpired;
    private Long fanId;
}
