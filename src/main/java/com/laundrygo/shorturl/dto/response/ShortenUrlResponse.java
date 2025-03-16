package com.laundrygo.shorturl.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ShortenUrlResponse {
    private String shortUrl;
}