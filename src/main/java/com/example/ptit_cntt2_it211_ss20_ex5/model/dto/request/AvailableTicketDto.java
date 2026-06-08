package com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableTicketDto {
    private Long ticketCategoryId;
    private String categoryName;
    private Double price;
    private Integer remainingQuantity;
}