package com.laundrygo.shorturl.service.service;

import com.laundrygo.shorturl.domain.UrlAccessLog;
import com.laundrygo.shorturl.domain.UrlMapping;
import com.laundrygo.shorturl.domain.UrlStats;
import com.laundrygo.shorturl.repository.UrlAccessLogRepository;
import com.laundrygo.shorturl.repository.UrlMappingRepository;
import com.laundrygo.shorturl.service.ShortUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private UrlMappingRepository urlMappingRepository;

    @Mock
    private UrlAccessLogRepository urlAccessLogRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    private final String ORIGINAL_URL = "https://www.example.com";
    private final String SHORT_URL = "Abc12345";

    @Test
    @DisplayName("기존에 없는 URL 단축 테스트")
    void shortenNewUrlTest() {
        // given
        when(urlMappingRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(null);
        when(urlMappingRepository.existsByShortUrl(anyString())).thenReturn(false);

        // when
        String shortUrl = shortUrlService.shortenUrl(ORIGINAL_URL);

        // then
        assertNotNull(shortUrl);
        assertEquals(8, shortUrl.length());
        verify(urlMappingRepository, times(1)).save(any(UrlMapping.class));
    }

    @Test
    @DisplayName("이미 존재하는 URL 단축 테스트")
    void shortenExistingUrlTest() {
        // given
        UrlMapping existingMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(ORIGINAL_URL)
                .shortUrl(SHORT_URL)
                .build();
        
        when(urlMappingRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(existingMapping);

        // when
        String shortUrl = shortUrlService.shortenUrl(ORIGINAL_URL);

        // then
        assertEquals(SHORT_URL, shortUrl);
        verify(urlMappingRepository, never()).save(any());
    }

    @Test
    @DisplayName("단축 URL로 원본 URL 조회 및 접근 로그 생성 테스트")
    void getOriginalUrlAndCreateAccessLogTest() {
        // given
        UrlMapping urlMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(ORIGINAL_URL)
                .shortUrl(SHORT_URL)
                .build();

        when(urlMappingRepository.findByShortUrl(SHORT_URL)).thenReturn(urlMapping);
        doNothing().when(urlAccessLogRepository).save(any(UrlAccessLog.class));

        // when
        String originalUrl = shortUrlService.getOriginalUrlAndCreateAccessLog(SHORT_URL);

        // then
        assertEquals(ORIGINAL_URL, originalUrl);
        verify(urlAccessLogRepository, times(1)).save(any(UrlAccessLog.class));
    }

    @Test
    @DisplayName("존재하지 않는 단축 URL 조회 테스트")
    void getOriginalUrlNotFoundTest() {
        // given
        when(urlMappingRepository.findByShortUrl(anyString())).thenReturn(null);

        // when
        String originalUrl = shortUrlService.getOriginalUrlAndCreateAccessLog("notExist");

        // then
        assertNull(originalUrl);
        verify(urlAccessLogRepository, never()).save(any());
    }


    @Test
    @DisplayName("단축 URL 통계 조회 테스트")
    void getShortUrlAccessStatsTest() {
        // given
        UrlMapping urlMapping = UrlMapping.builder()
                .id(1L)
                .originalUrl(ORIGINAL_URL)
                .shortUrl(SHORT_URL)
                .build();

        LocalDateTime now = LocalDateTime.now();
        List<UrlAccessLog> accessLogs = new ArrayList<>();
        accessLogs.add(UrlAccessLog.builder().urlMappingId(1L).accessedAt(now).build());
        accessLogs.add(UrlAccessLog.builder().urlMappingId(1L).accessedAt(now.minusMinutes(30)).build());

        when(urlMappingRepository.findByShortUrl(SHORT_URL)).thenReturn(urlMapping);
        when(urlAccessLogRepository.findAccessLogsByUrlMappingIdAndTimeRange(
                eq(1L), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(accessLogs);

        // when
        UrlStats stats = shortUrlService.getShortUrlAccessStats(SHORT_URL);

        // then
        assertNotNull(stats);
        assertEquals(ORIGINAL_URL, stats.getOriginalUrl());
        assertNotNull(stats.getHourlyStats());

        // 시간별 통계는 24시간을 모두 포함해야 함
        assertEquals(24, stats.getHourlyStats().size());
    }
}