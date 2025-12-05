package com.example.crypto.controller;

import com.example.crypto.dto.*;
import com.example.crypto.entity.Category;
import com.example.crypto.repository.CategoryRepository;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryRepository repo;
    public CategoryController(CategoryRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<PageResult<CategoryDto>> list(
            @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "per_page", defaultValue = "10") int perPage
    ) {
        int pageIndex = Math.max(0, pageNum - 1);
        Pageable p = PageRequest.of(pageIndex, perPage, Sort.by("name").ascending());
        Page<Category> page = repo.findAll(p);
        var items = page.getContent().stream().map(c -> new CategoryDto(c.getId(), c.getName())).toList();
        return ResponseEntity.ok(new PageResult<>(pageNum, perPage, page.getTotalPages(), page.getTotalElements(), items));
    }
}

