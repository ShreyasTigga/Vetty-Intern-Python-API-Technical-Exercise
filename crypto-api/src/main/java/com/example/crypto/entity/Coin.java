package com.example.crypto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Coin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String coinId; // slug e.g. "bitcoin" (for CoinGecko)
    private String name;
    private String symbol;

    @ManyToOne
    private Category category;
}
