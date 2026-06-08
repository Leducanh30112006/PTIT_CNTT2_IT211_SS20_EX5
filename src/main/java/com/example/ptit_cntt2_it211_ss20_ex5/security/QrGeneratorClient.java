package com.example.ptit_cntt2_it211_ss20_ex5.security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.PartnerQrRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response.PartnerQrResponse;

@FeignClient(name = "qrGeneratorClient", url = "${api.qr-service.url}")
public interface QrGeneratorClient {

    @PostMapping
    PartnerQrResponse generateQrCode(@RequestBody PartnerQrRequest request);
}