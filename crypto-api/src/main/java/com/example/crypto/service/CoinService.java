package com.example.crypto.service;

import com.example.crypto.dto.*;
import com.example.crypto.entity.Coin;
import com.example.crypto.repository.CoinRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.*;
import java.math.BigDecimal;

@Service
public class CoinService {

    private final CoinRepository repo;
    private final MarketDataService marketService;

    public CoinService(CoinRepository repo, MarketDataService marketService) {
        this.repo = repo;
        this.marketService = marketService;
    }

    public PageResult<CoinDto> listCoins(int pageNum, int perPage, String category) {
        int pageIndex = Math.max(0, pageNum - 1);
        Pageable p = PageRequest.of(pageIndex, perPage, Sort.by("name").ascending());
        Page<Coin> page = (category == null || category.isBlank())
                ? repo.findAll(p)
                : repo.findByCategory_Name(category, p);

        List<String> ids = page.getContent().stream().map(Coin::getCoinId).filter(Objects::nonNull).collect(Collectors.toList());
        String idsParam = String.join(",", ids);

        Map<String, Map<String, Object>> prices = ids.isEmpty() ? Collections.emptyMap() : marketService.fetch(idsParam);

        List<CoinDto> dtos = page.getContent().stream().map(c -> {
            CoinDto dto = new CoinDto();
            dto.setId(c.getId());
            dto.setCoinId(c.getCoinId());
            dto.setName(c.getName());
            dto.setSymbol(c.getSymbol());
            if (c.getCategory() != null) {
                dto.setCategory(new CategoryDto(c.getCategory().getId(), c.getCategory().getName()));
            }
            Map<String, Object> raw = prices.getOrDefault(c.getCoinId(), Collections.emptyMap());
            dto.setMarketData(mapToMarketData(raw));
            return dto;
        }).collect(Collectors.toList());

        return new PageResult<>(pageNum, perPage, page.getTotalPages(), page.getTotalElements(), dtos);
    }

    public CoinDto getById(Long id) {
        Coin c = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Coin not found"));
        CoinDto dto = new CoinDto();
        dto.setId(c.getId());
        dto.setCoinId(c.getCoinId());
        dto.setName(c.getName());
        dto.setSymbol(c.getSymbol());
        if (c.getCategory() != null) dto.setCategory(new CategoryDto(c.getCategory().getId(), c.getCategory().getName()));

        Map<String, Map<String, Object>> prices = marketService.fetch(c.getCoinId());
        dto.setMarketData(mapToMarketData(prices.getOrDefault(c.getCoinId(), Collections.emptyMap())));
        return dto;
    }

    private MarketDataDto mapToMarketData(Map<String, Object> raw) {
        MarketDataDto m = new MarketDataDto();
        if (raw == null || raw.isEmpty()) return m;
        m.setPriceInr(toBD(raw.get("inr")));
        m.setPriceCad(toBD(raw.get("cad")));
        m.setMarketCapInr(toBD(raw.get("inr_market_cap")));
        m.setMarketCapCad(toBD(raw.get("cad_market_cap")));
        m.setChange24hInr(toBD(raw.get("inr_24h_change")));
        m.setChange24hCad(toBD(raw.get("cad_24h_change")));
        return m;
    }

    private BigDecimal toBD(Object o) {
        if (o == null) return null;
        try {
            return new BigDecimal(o.toString());
        } catch (Exception ex) {
            return null;
        }
    }
}
