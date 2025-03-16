package com.laundrygo.shorturl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Builder
public class UrlMapping {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
}