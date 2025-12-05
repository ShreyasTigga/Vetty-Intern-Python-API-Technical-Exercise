package com.example.crypto.config;

import com.example.crypto.repository.CategoryRepository;
import com.example.crypto.repository.CoinRepository;
import com.example.crypto.repository.UserRepository;

import com.example.crypto.entity.Category;
import com.example.crypto.entity.Coin;
import com.example.crypto.entity.AppUser;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(CategoryRepository catRepo,
                               CoinRepository coinRepo,
                               UserRepository userRepo) {
        return args -> {
            var defi = catRepo.save(new Category(null, "DeFi"));
            var store = catRepo.save(new Category(null, "StoreOfValue"));
            coinRepo.save(new Coin(null, "bitcoin", "Bitcoin", "BTC", store));
            coinRepo.save(new Coin(null, "ethereum", "Ethereum", "ETH", defi));
            userRepo.save(new AppUser(null, "demo", "password"));
        };
    }
}
