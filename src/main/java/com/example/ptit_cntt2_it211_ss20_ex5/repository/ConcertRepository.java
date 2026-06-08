package com.example.ptit_cntt2_it211_ss20_ex5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ptit_cntt2_it211_ss20_ex5.model.entity.Concert;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}