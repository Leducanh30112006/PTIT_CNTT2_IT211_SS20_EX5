package com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerQrResponse {
    private String qrCodeUrl;
}