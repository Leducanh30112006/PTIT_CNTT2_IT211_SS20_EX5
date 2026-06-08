package com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<CheckoutItemRequest> items;
}