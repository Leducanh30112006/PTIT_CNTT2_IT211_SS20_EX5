package com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}