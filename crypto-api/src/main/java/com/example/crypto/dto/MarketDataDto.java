package com.example.crypto.dto;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MarketDataDto {
    private BigDecimal priceInr;
    private BigDecimal priceCad;
    private BigDecimal marketCapInr;
    private BigDecimal marketCapCad;
    private BigDecimal change24hInr;
    private BigDecimal change24hCad;
}
