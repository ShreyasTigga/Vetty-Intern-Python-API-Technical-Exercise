package com.example.crypto.controller;

import com.example.crypto.dto.*;
import com.example.crypto.service.CoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coins")
public class CoinController {
    private final CoinService coinService;
    public CoinController(CoinService coinService) { this.coinService = coinService; }

    @GetMapping
    public ResponseEntity<PageResult<CoinDto>> list(
            @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "per_page", defaultValue = "10") int perPage,
            @RequestParam(value = "category", required = false) String category
    ) {
        return ResponseEntity.ok(coinService.listCoins(pageNum, perPage, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoinDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(coinService.getById(id));
    }
}
