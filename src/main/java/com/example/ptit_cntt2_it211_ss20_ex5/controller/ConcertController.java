package com.example.ptit_cntt2_it211_ss20_ex5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.AvailableTicketDto;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.CheckoutRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.BookingOrder;
import com.example.ptit_cntt2_it211_ss20_ex5.service.ConcertService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/tickets/available")
    public ResponseEntity<Map<Long, List<AvailableTicketDto>>> getAvailableTickets() {
        return ResponseEntity.ok(concertService.getAvailableTicketsGroupedByConcert());
    }

    @PostMapping("/bookings/checkout")
    public ResponseEntity<BookingOrder> checkout(@RequestBody CheckoutRequest request) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(concertService.checkout(request, currentEmail));
    }
}