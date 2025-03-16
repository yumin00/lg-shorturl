package com.laundrygo.shorturl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @Builder
public class UrlAccessLog {
    private Long id;
    private Long urlMappingId;
    private LocalDateTime accessedAt;
}