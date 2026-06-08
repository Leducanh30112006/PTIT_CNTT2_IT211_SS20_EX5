package com.example.ptit_cntt2_it211_ss20_ex5.data;

import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.Concert;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.FanAccount;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.TicketCategory;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.ConcertRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.FanAccountRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FanAccountRepository fanAccountRepository;
    private final ConcertRepository concertRepository;
    private final TicketCategoryRepository ticketCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        FanAccount fan = FanAccount.builder()
                .email("hoan@gmail.com")
                .password(passwordEncoder.encode("password123"))
                .fullName("Nguyễn Huy Hoàn")
                .role("FAN")
                .build();
        fanAccountRepository.save(fan);

        Concert concert1 = Concert.builder()
                .name("Sky Tour 2026")
                .showDate(LocalDate.of(2026, 8, 15))
                .location("My Dinh Stadium")
                .build();
        concertRepository.save(concert1);

        ticketCategoryRepository.save(TicketCategory.builder().concertId(concert1.getId()).name("VVIP").price(5000000.0).remainingQuantity(10).build());
        ticketCategoryRepository.save(TicketCategory.builder().concertId(concert1.getId()).name("VIP").price(3000000.0).remainingQuantity(50).build());
        ticketCategoryRepository.save(TicketCategory.builder().concertId(concert1.getId()).name("GA").price(1000000.0).remainingQuantity(0).build());
    }
}