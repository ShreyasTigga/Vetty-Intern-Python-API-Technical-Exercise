package com.example.crypto.repository;

import com.example.crypto.entity.Coin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Page<Coin> findByCategory_Name(String categoryName, Pageable pageable);
}
