package com.laundrygo.shorturl.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
public class UrlStatsResponse {
    private String originalUrl;
    private List<HourlyAccessStatsDto> hourlyStats;
}