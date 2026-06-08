package com.example.ptit_cntt2_it211_ss20_ex5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.BookingItem;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
}