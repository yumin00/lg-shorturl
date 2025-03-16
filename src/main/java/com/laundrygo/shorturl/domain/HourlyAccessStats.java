package com.laundrygo.shorturl.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HourlyAccessStats {
    private LocalDateTime dateTime;
    private Long accessCount;
}