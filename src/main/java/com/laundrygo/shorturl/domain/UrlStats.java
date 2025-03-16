package com.laundrygo.shorturl.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
public class UrlStats {
    private String originalUrl;
    private List<HourlyAccessStats> hourlyStats;
}