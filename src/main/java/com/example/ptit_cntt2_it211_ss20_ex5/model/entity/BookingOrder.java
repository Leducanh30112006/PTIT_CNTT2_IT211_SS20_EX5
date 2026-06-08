package com.example.ptit_cntt2_it211_ss20_ex5.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fanId;
    private Double totalAmount;
    private String qrCodeUrl;
    private String status;
}