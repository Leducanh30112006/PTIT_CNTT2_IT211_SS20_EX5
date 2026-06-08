package com.example.ptit_cntt2_it211_ss20_ex5.controller;

import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.PartnerQrRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response.PartnerQrResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock/partner")
public class PartnerMockController {

    @PostMapping
    public ResponseEntity<PartnerQrResponse> mockQrGenerate(@RequestBody PartnerQrRequest request) {
        PartnerQrResponse response = new PartnerQrResponse("https://mock.com/qr/" + request.getOrderId() + ".png");
        return ResponseEntity.ok(response);
    }
}