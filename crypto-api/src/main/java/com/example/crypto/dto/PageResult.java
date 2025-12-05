package com.example.crypto.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PageResult<T> {
    private int page_num;
    private int per_page;
    private int total_pages;
    private long total_items;
    private List<T> items;
}
