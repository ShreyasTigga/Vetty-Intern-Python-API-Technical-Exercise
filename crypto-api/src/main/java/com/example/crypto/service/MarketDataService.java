package com.example.crypto.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;
import java.math.BigDecimal;

@Service
public class MarketDataService {
    private final RestTemplate rest = new RestTemplate();
    private static final String URL = "https://api.coingecko.com/api/v3/simple/price";

    /**
     * Fetch minimal market data for given coin slugs. Returns a map coinId -> response map
     * The response JSON looks like:
     * { "bitcoin": { "inr": 12345, "cad": 2345, "inr_market_cap": ..., "inr_24h_change": ... }, ... }
     */
    public Map<String, Map<String, Object>> fetch(String commaSeparatedIds) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("ids", commaSeparatedIds)
                .queryParam("vs_currencies", "inr,cad")
                .queryParam("include_market_cap", "true")
                .queryParam("include_24hr_change", "true");

        Map<String, Object> resp = rest.getForObject(uri.toUriString(), Map.class);
        if (resp == null) return Collections.emptyMap();

        Map<String, Map<String, Object>> typed = new HashMap<>();
        for (Map.Entry<String, Object> e : resp.entrySet()) {
            typed.put(e.getKey(), (Map<String, Object>) e.getValue());
        }
        return typed;
    }
}
