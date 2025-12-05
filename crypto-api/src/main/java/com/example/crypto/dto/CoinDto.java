package com.example.crypto.dto;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CoinDto {
    private Long id;
    private String coinId;
    private String name;
    private String symbol;
    private CategoryDto category;
    private MarketDataDto marketData;
}
