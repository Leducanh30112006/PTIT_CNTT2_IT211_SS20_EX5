package com.example.ptit_cntt2_it211_ss20_ex5.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.AvailableTicketDto;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.CheckoutRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.request.PartnerQrRequest;
import com.example.ptit_cntt2_it211_ss20_ex5.model.dto.response.PartnerQrResponse;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.BookingItem;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.BookingOrder;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.FanAccount;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.TicketCategory;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.*;
import com.example.ptit_cntt2_it211_ss20_ex5.security.QrGeneratorClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final BookingOrderRepository bookingOrderRepository;
    private final BookingItemRepository bookingItemRepository;
    private final FanAccountRepository fanAccountRepository;
    private final QrGeneratorClient qrGeneratorClient;

    public Map<Long, List<AvailableTicketDto>> getAvailableTicketsGroupedByConcert() {
        List<TicketCategory> allCategories = ticketCategoryRepository.findAll();

        return allCategories.stream()
                .filter(category -> category.getRemainingQuantity() > 0)
                .map(cat -> new AvailableTicketDto(cat.getId(), cat.getName(), cat.getPrice(), cat.getRemainingQuantity()))
                .collect(Collectors.groupingBy(
                        dto -> ticketCategoryRepository.findById(dto.getTicketCategoryId()).get().getConcertId()
                ));
    }

    @Transactional
    public BookingOrder checkout(CheckoutRequest request, String fanEmail) {
        FanAccount fan = fanAccountRepository.findByEmail(fanEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người mua"));

        double totalAmount = request.getItems().stream()
                .mapToDouble(item -> {
                    TicketCategory cat = ticketCategoryRepository.findById(item.getTicketCategoryId())
                            .orElseThrow(() -> new RuntimeException("Hạng vé không tồn tại"));
                    if (cat.getRemainingQuantity() < item.getQuantity()) {
                        throw new RuntimeException("Hạng vé " + cat.getName() + " không đủ số lượng tồn kho");
                    }
                    return cat.getPrice() * item.getQuantity();
                })
                .sum();

        BookingOrder order = BookingOrder.builder()
                .fanId(fan.getId())
                .totalAmount(totalAmount)
                .status("PENDING")
                .build();
        final BookingOrder savedOrder = bookingOrderRepository.save(order);

        request.getItems().stream().forEach(item -> {
            TicketCategory cat = ticketCategoryRepository.findById(item.getTicketCategoryId()).get();

            cat.setRemainingQuantity(cat.getRemainingQuantity() - item.getQuantity());
            ticketCategoryRepository.save(cat);

            BookingItem bookingItem = BookingItem.builder()
                    .orderId(savedOrder.getId())
                    .ticketCategoryId(cat.getId())
                    .quantity(item.getQuantity())
                    .subTotal(cat.getPrice() * item.getQuantity())
                    .build();
            bookingItemRepository.save(bookingItem);
        });

        PartnerQrRequest partnerRequest = new PartnerQrRequest();
        partnerRequest.setOrderId(savedOrder.getId());
        partnerRequest.setFanEmail(fan.getEmail());

        try {
            PartnerQrResponse partnerResponse = qrGeneratorClient.generateQrCode(partnerRequest);
            savedOrder.setQrCodeUrl(partnerResponse.getQrCodeUrl());
            savedOrder.setStatus("SUCCESS");
        } catch (Exception e) {
            savedOrder.setQrCodeUrl(null);
            savedOrder.setStatus("PENDING_QR");
        }

        return bookingOrderRepository.save(savedOrder);
    }
}